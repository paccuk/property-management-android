package org.stkachenko.propertymanagement.core.data.model.payment

import org.stkachenko.propertymanagement.core.database.model.payment.InvoiceEntity
import org.stkachenko.propertymanagement.core.network.model.payment.NetworkInvoice

fun NetworkInvoice.asEntity() = InvoiceEntity(
    id = id,
    scheduleId = scheduleId,
    invoiceNumber = invoiceNumber,
    issuedAt = issuedAt,
    status = status,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun InvoiceEntity.asNetworkModel() = NetworkInvoice(
    id = id,
    scheduleId = scheduleId,
    invoiceNumber = invoiceNumber,
    issuedAt = issuedAt,
    status = status,
    createdAt = createdAt,
    updatedAt = updatedAt,
)