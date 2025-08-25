package org.stkachenko.propertymanagement.core.model.data.payment

import java.util.Date

data class PaymentSchedule(
    val id: String,
    val agreementId: String,
    val dueDate: Date,
    val amount: Double,
    val currency: String,
    val description: String,
    val createdAt: Long,
    val updatedAt: Long,
)
