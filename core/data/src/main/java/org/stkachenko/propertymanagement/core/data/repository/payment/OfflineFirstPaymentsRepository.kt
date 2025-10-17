package org.stkachenko.propertymanagement.core.data.repository.payment

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.stkachenko.propertymanagement.core.data.model.payment.asEntity
import org.stkachenko.propertymanagement.core.data.model.payment.asNetworkModel
import org.stkachenko.propertymanagement.core.database.dao.payment.PaymentDao
import org.stkachenko.propertymanagement.core.database.model.payment.asExternalModel
import org.stkachenko.propertymanagement.core.datastore.user_preferences.PmPreferencesDataSource
import org.stkachenko.propertymanagement.core.model.data.payment.Payment
import org.stkachenko.propertymanagement.core.network.ProtectedNetworkDataSource
import javax.inject.Inject

internal class OfflineFirstPaymentsRepository @Inject constructor(
    private val paymentDao: PaymentDao,
    private val network: ProtectedNetworkDataSource,
) : PaymentsRepository {

    override fun getPayment(id: String): Flow<Payment> =
        paymentDao.getPaymentEntity(id).map { it.asExternalModel() }

    override suspend fun syncWith(
        pmPreferences: PmPreferencesDataSource,
        ioDispatcher: CoroutineDispatcher,
    ): Boolean = withContext(ioDispatcher) {
        val lastSync = pmPreferences.getLastPaymentSyncTime()

        val localChanged = paymentDao.getPaymentsUpdatedAfter(lastSync)
        if (localChanged.isNotEmpty()) {
            val networkModels = localChanged.map { it.asNetworkModel() }
            network.updatePayments(networkModels)
        }
        val updatedFromBackend = network.getPaymentsUpdatedAfter(lastSync)
        if (updatedFromBackend.isNotEmpty()) {
            paymentDao.upsertPayments(updatedFromBackend.map { it.asEntity() })
        }
        val newSyncTime =
            (localChanged.map { it.updatedAt } + updatedFromBackend.map { it.updatedAt }).maxOrNull()
                ?: lastSync
        if (newSyncTime > lastSync) {
            pmPreferences.setLastPaymentSyncTime(newSyncTime)
        }
        return@withContext true
    }
}