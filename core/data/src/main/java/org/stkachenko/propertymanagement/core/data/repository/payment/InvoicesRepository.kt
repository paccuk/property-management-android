package org.stkachenko.propertymanagement.core.data.repository.payment

import kotlinx.coroutines.flow.Flow
import org.stkachenko.propertymanagement.core.data.Syncable
import org.stkachenko.propertymanagement.core.model.data.payment.Invoice

interface InvoicesRepository : Syncable {
    fun getInvoice(id: String): Flow<Invoice>
}