package org.stkachenko.propertymanagement.core.model.data.payment

data class Invoice(
    val id: String,
    val scheduleId: String,
    val invoiceNumber: String,
    val issuedAt: Long,
    val status: String,
    val createdAt: Long,
    val updatedAt: Long,
)
