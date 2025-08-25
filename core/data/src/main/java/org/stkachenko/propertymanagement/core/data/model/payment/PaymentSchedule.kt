package org.stkachenko.propertymanagement.core.data.model.payment

import org.stkachenko.propertymanagement.core.database.model.payment.PaymentScheduleEntity
import org.stkachenko.propertymanagement.core.network.model.payment.NetworkPaymentSchedule

fun NetworkPaymentSchedule.asEntity() = PaymentScheduleEntity(
    id = id,
    agreementId = agreementId,
    dueDate = dueDate,
    amount = amount,
    currency = currency,
    description = description,
    createdAt = createdAt,
    updatedAt = updatedAt,
)