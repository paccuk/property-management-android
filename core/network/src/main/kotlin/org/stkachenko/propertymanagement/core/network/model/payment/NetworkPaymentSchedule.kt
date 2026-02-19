package org.stkachenko.propertymanagement.core.network.model.payment

import kotlinx.serialization.Serializable

@Serializable
data class NetworkPaymentSchedule(
    val id: String,
    val agreementId: String,
    val dueDate: Long,
    val amount: Double,
    val currency: String,
    val description: String,
    val createdAt: Long,
    val updatedAt: Long,
)
