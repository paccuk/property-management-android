package org.stkachenko.propertymanagement.core.network.model.payment

import kotlinx.serialization.Serializable

@Serializable
data class NetworkInvoice(
    val id: String,
    val scheduleId: String,
    val invoiceNumber: String,
    val issuedAt: Long,
    val status: String,
    val createdAt: Long,
    val updatedAt: Long,
)
