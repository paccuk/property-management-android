package org.stkachenko.propertymanagement.core.database.model.payment

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import org.stkachenko.propertymanagement.core.database.model.user.UserEntity
import org.stkachenko.propertymanagement.core.model.data.payment.Payment

@Entity(
    tableName = "payments",
    foreignKeys = [
        ForeignKey(
            entity = InvoiceEntity::class,
            parentColumns = ["id"],
            childColumns = ["invoiceId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["payerId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["payeeId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index("invoiceId"),
        Index("payerId"),
        Index("payeeId"),
    ],
)
data class PaymentEntity(
    @PrimaryKey
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

fun PaymentEntity.asExternalModel() = Payment(
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