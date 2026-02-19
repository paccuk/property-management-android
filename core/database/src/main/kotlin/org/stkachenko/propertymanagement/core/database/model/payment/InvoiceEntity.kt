package org.stkachenko.propertymanagement.core.database.model.payment

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import org.stkachenko.propertymanagement.core.model.data.payment.Invoice

@Entity(
    tableName = "invoices",
    foreignKeys = [
        ForeignKey(
            entity = PaymentScheduleEntity::class,
            parentColumns = ["id"],
            childColumns = ["scheduleId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index("scheduleId"),
    ],
)
data class InvoiceEntity(
    @PrimaryKey
    val id: String,
    val scheduleId: String,
    val invoiceNumber: String,
    val issuedAt: Long,
    val status: String,
    val createdAt: Long,
    val updatedAt: Long
)

fun InvoiceEntity.asExternalModel() = Invoice(
    id = id,
    scheduleId = scheduleId,
    invoiceNumber = invoiceNumber,
    issuedAt = issuedAt,
    status = status,
    createdAt = createdAt,
    updatedAt = updatedAt
)