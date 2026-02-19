package org.stkachenko.propertymanagement.core.data.repository.payment

import kotlinx.coroutines.flow.Flow
import org.stkachenko.propertymanagement.core.data.Syncable
import org.stkachenko.propertymanagement.core.model.data.payment.PaymentSchedule

interface PaymentSchedulesRepository: Syncable {
    fun getPaymentSchedulesByAgreementId(id: String): Flow<List<PaymentSchedule>>
}

