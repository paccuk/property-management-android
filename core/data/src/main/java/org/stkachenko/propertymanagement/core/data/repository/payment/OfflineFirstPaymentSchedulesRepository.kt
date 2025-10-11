package org.stkachenko.propertymanagement.core.data.repository.payment

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.stkachenko.propertymanagement.core.data.Synchronizer
import org.stkachenko.propertymanagement.core.data.changeListSync
import org.stkachenko.propertymanagement.core.data.model.payment.asEntity
import org.stkachenko.propertymanagement.core.database.dao.payment.PaymentScheduleDao
import org.stkachenko.propertymanagement.core.database.model.payment.PaymentScheduleEntity
import org.stkachenko.propertymanagement.core.database.model.payment.asExternalModel
import org.stkachenko.propertymanagement.core.datastore.ChangeListVersions
import org.stkachenko.propertymanagement.core.model.data.payment.PaymentSchedule
import org.stkachenko.propertymanagement.core.network.ProtectedNetworkDataSource
import org.stkachenko.propertymanagement.core.network.model.payment.NetworkPaymentSchedule
import javax.inject.Inject

internal class OfflineFirstPaymentSchedulesRepository @Inject constructor(
    private val paymentScheduleDao: PaymentScheduleDao,
    private val network: ProtectedNetworkDataSource,
) : PaymentSchedulesRepository {
    override fun getPaymentSchedulesByAgreementId(id: String): Flow<List<PaymentSchedule>> =
        paymentScheduleDao.getPaymentScheduleEntitiesByAgreementId(id)
            .map { it.map(PaymentScheduleEntity::asExternalModel) }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean =
        synchronizer.changeListSync(
            versionReader = ChangeListVersions::paymentScheduleVersion,
            changeListFetcher = { currentVersion ->
                network.getPaymentScheduleChangeList(after = currentVersion)
            },
            versionUpdater = { latestVersion ->
                copy(paymentScheduleVersion = latestVersion)
            },
            modelDeleter = paymentScheduleDao::deletePaymentSchedules,
            modelUpdater = { changedIds ->
                val networkPaymentSchedule = network.getPaymentSchedules(ids = changedIds)
                paymentScheduleDao.upsertPaymentSchedules(
                    entities = networkPaymentSchedule.map(NetworkPaymentSchedule::asEntity)
                )
            },
        )
}