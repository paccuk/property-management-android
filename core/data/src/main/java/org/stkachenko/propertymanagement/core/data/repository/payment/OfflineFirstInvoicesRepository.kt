package org.stkachenko.propertymanagement.core.data.repository.payment

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.stkachenko.propertymanagement.core.data.model.payment.asEntity
import org.stkachenko.propertymanagement.core.data.model.payment.asNetworkModel
import org.stkachenko.propertymanagement.core.database.dao.payment.InvoiceDao
import org.stkachenko.propertymanagement.core.database.model.payment.asExternalModel
import org.stkachenko.propertymanagement.core.datastore.user_preferences.PmPreferencesDataSource
import org.stkachenko.propertymanagement.core.model.data.payment.Invoice
import org.stkachenko.propertymanagement.core.network.ProtectedNetworkDataSource
import javax.inject.Inject

internal class OfflineFirstInvoicesRepository @Inject constructor(
    private val invoiceDao: InvoiceDao,
    private val network: ProtectedNetworkDataSource,
) : InvoicesRepository {

    override fun getInvoice(id: String): Flow<Invoice> =
        invoiceDao.getInvoiceEntity(id).map { it.asExternalModel() }

    override suspend fun syncWith(
        pmPreferences: PmPreferencesDataSource,
        ioDispatcher: CoroutineDispatcher,
    ): Boolean = withContext(ioDispatcher) {
        val lastSync = pmPreferences.getLastInvoiceSyncTime()
        val localChanged = invoiceDao.getInvoicesUpdatedAfter(lastSync)
        if (localChanged.isNotEmpty()) {
            val networkModels = localChanged.map { it.asNetworkModel() }
            network.updateInvoices(networkModels)
        }
        val updatedFromBackend = network.getInvoicesUpdatedAfter(lastSync)
        if (updatedFromBackend.isNotEmpty()) {
            invoiceDao.upsertInvoices(updatedFromBackend.map { it.asEntity() })
        }
        val newSyncTime =
            (localChanged.map { it.updatedAt } + updatedFromBackend.map { it.updatedAt }).maxOrNull()
                ?: lastSync
        if (newSyncTime > lastSync) {
            pmPreferences.setLastInvoiceSyncTime(newSyncTime)
        }
        return@withContext true
    }
}