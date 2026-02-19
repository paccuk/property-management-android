package org.stkachenko.propertymanagement.core.database.model.payment

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import org.stkachenko.propertymanagement.core.database.model.rental.RentalAgreementEntity
import org.stkachenko.propertymanagement.core.model.data.payment.PaymentSchedule
import java.util.Date

@Entity(
    tableName = "payment_schedules",
    foreignKeys = [
        ForeignKey(
            entity = RentalAgreementEntity::class,
            parentColumns = ["id"],
            childColumns = ["agreementId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index("agreementId"),
    ],
)
data class PaymentScheduleEntity(
    @PrimaryKey
    val id: String,
    val agreementId: String,
    val dueDate: Long,
    val amount: Double,
    val currency: String,
    val description: String,
    val createdAt: Long,
    val updatedAt: Long,
)

fun PaymentScheduleEntity.asExternalModel() = PaymentSchedule(
    id = id,
    agreementId = agreementId,
    dueDate = Date(dueDate),
    amount = amount,
    currency = currency,
    description = description,
    createdAt = createdAt,
    updatedAt = updatedAt,
)