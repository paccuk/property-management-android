package org.stkachenko.propertymanagement.core.network.model.payment

import kotlinx.serialization.Serializable

@Serializable
data class NetworkPayment(
    val id: String,
    val invoiceId: String,
    val payerId: String,
    val payeeId: String,
    val amount: Double = 0.0,
    val currency: String = "",
    val paymentMethod: String = "",
    val paidAt: Long = 0L,
    val status: String = "",
    val transactionReference: String = "",
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
)