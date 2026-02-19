package org.stkachenko.propertymanagement.core.model.data.payment

data class Payment(
    val id: String,
    val invoiceId: String,
    val payerId: String,
    val payeeId: String,
    val amount: Double,
    val currency: String,
    val paymentMethod: String,
    val paidAt: Long,
    val status: String,
    val transactionReference: String,
    val createdAt: Long,
    val updatedAt: Long,
)