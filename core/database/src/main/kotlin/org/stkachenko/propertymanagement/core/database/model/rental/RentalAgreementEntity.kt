package org.stkachenko.propertymanagement.core.database.model.rental

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import org.stkachenko.propertymanagement.core.database.model.user.UserEntity
import org.stkachenko.propertymanagement.core.model.data.rental.RentalAgreement
import java.util.Date

@Entity(
    tableName = "rental_agreements",
    foreignKeys = [
        ForeignKey(
            entity = RentalOfferEntity::class,
            parentColumns = ["id"],
            childColumns = ["offerId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["tenantId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index("offerId"),
        Index("tenantId"),
    ],
)
data class RentalAgreementEntity(
    @PrimaryKey
    val id: String,
    val offerId: String,
    val tenantId: String,
    val startDate: Date,
    val endDate: Date,
    val status: String,
    val createdAt: Long,
    val updatedAt: Long,
)

fun RentalAgreementEntity.asExternalModel() = RentalAgreement(
    id = id,
    offerId = offerId,
    tenantId = tenantId,
    startDate = startDate,
    endDate = endDate,
    status = status,
    createdAt = createdAt,
    updatedAt = updatedAt,
)