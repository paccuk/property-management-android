package org.stkachenko.propertymanagement.core.data.repository.payment

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.stkachenko.propertymanagement.core.data.Synchronizer
import org.stkachenko.propertymanagement.core.data.changeListSync
import org.stkachenko.propertymanagement.core.data.model.payment.asEntity
import org.stkachenko.propertymanagement.core.database.dao.payment.PaymentDao
import org.stkachenko.propertymanagement.core.database.model.payment.asExternalModel
import org.stkachenko.propertymanagement.core.datastore.ChangeListVersions
import org.stkachenko.propertymanagement.core.model.data.payment.Payment
import org.stkachenko.propertymanagement.core.network.ProtectedNetworkDataSource
import org.stkachenko.propertymanagement.core.network.model.payment.NetworkPayment
import javax.inject.Inject

internal class OfflineFirstPaymentsRepository @Inject constructor(
    private val paymentDao: PaymentDao,
    private val network: ProtectedNetworkDataSource,
) : PaymentsRepository {

    override fun getPayment(id: String): Flow<Payment> =
        paymentDao.getPaymentEntity(id).map { it.asExternalModel() }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean =
        synchronizer.changeListSync(
            versionReader = ChangeListVersions::paymentVersion,
            changeListFetcher = { currentVersion ->
                network.getPaymentChangeList(after = currentVersion)
            },
            versionUpdater = { latestVersion ->
                copy(paymentVersion = latestVersion)
            },
            modelDeleter = paymentDao::deletePayments,
            modelUpdater = { changedIds ->
                val networkPayments = network.getPayments(ids = changedIds)
                paymentDao.upsertPayments(
                    entities = networkPayments.map(NetworkPayment::asEntity)
                )
            },
        )
}