package org.stkachenko.propertymanagement.core.data.model.payment

import org.stkachenko.propertymanagement.core.database.model.payment.PaymentEntity
import org.stkachenko.propertymanagement.core.network.model.payment.NetworkPayment

fun NetworkPayment.asEntity() = PaymentEntity(
    id = id,
    invoiceId = invoiceId,
    payerId = payerId,
    payeeId = payeeId,
    amount = amount,
    currency = currency,
    paymentMethod = paymentMethod,
    paidAt = paidAt,
    status = status,
    transactionReference = transactionReference,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun PaymentEntity.asNetworkModel() = NetworkPayment(
    id = id,
    invoiceId = invoiceId,
    payerId = payerId,
    payeeId = payeeId,
    amount = amount,
    currency = currency,
    paymentMethod = paymentMethod,
    paidAt = paidAt,
    status = status,
    transactionReference = transactionReference,
    createdAt = createdAt,
    updatedAt = updatedAt,
)