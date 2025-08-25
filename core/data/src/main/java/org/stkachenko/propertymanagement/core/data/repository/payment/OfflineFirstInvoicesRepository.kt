package org.stkachenko.propertymanagement.core.data.repository.payment

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.stkachenko.propertymanagement.core.data.Synchronizer
import org.stkachenko.propertymanagement.core.data.changeListSync
import org.stkachenko.propertymanagement.core.data.model.payment.asEntity
import org.stkachenko.propertymanagement.core.database.dao.payment.InvoiceDao
import org.stkachenko.propertymanagement.core.database.model.payment.asExternalModel
import org.stkachenko.propertymanagement.core.datastore.ChangeListVersions
import org.stkachenko.propertymanagement.core.model.data.payment.Invoice
import org.stkachenko.propertymanagement.core.network.PmNetworkDataSource
import org.stkachenko.propertymanagement.core.network.model.payment.NetworkInvoice
import javax.inject.Inject

internal class OfflineFirstInvoicesRepository @Inject constructor(
    private val invoiceDao: InvoiceDao,
    private val network: PmNetworkDataSource,
) : InvoicesRepository {

    override fun getInvoice(id: String): Flow<Invoice> =
        invoiceDao.getInvoiceEntity(id).map { it.asExternalModel() }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean =
        synchronizer.changeListSync(
            versionReader = ChangeListVersions::invoiceVersion,
            changeListFetcher = { currentVersion ->
                network.getInvoiceChangeList(after = currentVersion)
            },
            versionUpdater = { latestVersion ->
                copy(invoiceVersion = latestVersion)
            },
            modelDeleter = invoiceDao::deleteInvoices,
            modelUpdater = { changedIds ->
                val networkInvoices = network.getInvoices(ids = changedIds)
                invoiceDao.upsertInvoices(
                    entities = networkInvoices.map(NetworkInvoice::asEntity),
                )
            },
        )
}