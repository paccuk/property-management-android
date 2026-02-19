package org.stkachenko.propertymanagement.core.data.repository.payment

import kotlinx.coroutines.flow.Flow
import org.stkachenko.propertymanagement.core.data.Syncable
import org.stkachenko.propertymanagement.core.model.data.payment.Payment

interface PaymentsRepository : Syncable {
    fun getPayment(id: String): Flow<Payment>
}
