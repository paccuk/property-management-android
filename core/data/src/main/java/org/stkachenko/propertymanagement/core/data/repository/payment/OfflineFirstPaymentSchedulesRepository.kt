package org.stkachenko.propertymanagement.core.data.repository.payment

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.stkachenko.propertymanagement.core.data.model.payment.asEntity
import org.stkachenko.propertymanagement.core.data.model.payment.asNetworkModel
import org.stkachenko.propertymanagement.core.database.dao.payment.PaymentScheduleDao
import org.stkachenko.propertymanagement.core.database.model.payment.PaymentScheduleEntity
import org.stkachenko.propertymanagement.core.database.model.payment.asExternalModel
import org.stkachenko.propertymanagement.core.datastore.user_preferences.PmPreferencesDataSource
import org.stkachenko.propertymanagement.core.model.data.payment.PaymentSchedule
import org.stkachenko.propertymanagement.core.network.ProtectedNetworkDataSource
import javax.inject.Inject

internal class OfflineFirstPaymentSchedulesRepository @Inject constructor(
    private val paymentScheduleDao: PaymentScheduleDao,
    private val network: ProtectedNetworkDataSource,
    private val pmPreferences: PmPreferencesDataSource,
) : PaymentSchedulesRepository {
    override fun getPaymentSchedulesByAgreementId(id: String): Flow<List<PaymentSchedule>> =
        paymentScheduleDao.getPaymentScheduleEntitiesByAgreementId(id)
            .map { it.map(PaymentScheduleEntity::asExternalModel) }

    override suspend fun syncWith(
        pmPreferences: PmPreferencesDataSource,
        ioDispatcher: CoroutineDispatcher,
    ): Boolean = withContext(ioDispatcher) {
        val lastSync = pmPreferences.getLastPaymentScheduleSyncTime()
        val localChanged = paymentScheduleDao.getPaymentSchedulesUpdatedAfter(lastSync)
        if (localChanged.isNotEmpty()) {
            val networkModels = localChanged.map { it.asNetworkModel() }
            network.updatePaymentSchedules(networkModels)
        }
        val updatedFromBackend = network.getPaymentSchedulesUpdatedAfter(lastSync)
        if (updatedFromBackend.isNotEmpty()) {
            paymentScheduleDao.upsertPaymentSchedules(updatedFromBackend.map { it.asEntity() })
        }
        val newSyncTime =
            (localChanged.map { it.updatedAt } + updatedFromBackend.map { it.updatedAt }).maxOrNull()
                ?: lastSync
        if (newSyncTime > lastSync) {
            pmPreferences.setLastPaymentScheduleSyncTime(newSyncTime)
        }
        return@withContext true
    }
}