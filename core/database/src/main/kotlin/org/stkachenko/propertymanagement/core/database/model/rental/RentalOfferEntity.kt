package org.stkachenko.propertymanagement.core.database.model.rental

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import org.stkachenko.propertymanagement.core.database.model.property.PropertyEntity
import org.stkachenko.propertymanagement.core.database.model.user.UserEntity
import org.stkachenko.propertymanagement.core.model.data.rental.RentalOffer
import java.util.Date

@Entity(
    tableName = "rental_offers",
    foreignKeys = [
        ForeignKey(
            entity = PropertyEntity::class,
            parentColumns = ["id"],
            childColumns = ["propertyId"],
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["ownerId"],
        ),
    ],
    indices = [
        Index("propertyId"),
        Index("ownerId"),
    ],
)
data class RentalOfferEntity(
    @PrimaryKey
    val id: String,
    val propertyId: String,
    val ownerId: String,
    val availableFrom: Date,
    val availableTo: Date,
    val minDuration: Long,
    val maxDuration: Long,
    val maxTenants: Int,
    val isActive: Boolean,
    val pricePerPerson: Double,
    val currency: String,
    val termsText: String,
    val createdAt: Long,
    val updatedAt: Long,
)

fun RentalOfferEntity.asExternalModel() = RentalOffer(
    id = id,
    propertyId = propertyId,
    ownerId = ownerId,
    availableFrom = availableFrom,
    availableTo = availableTo,
    minDuration = minDuration,
    maxDuration = maxDuration,
    maxTenants = maxTenants,
    isActive = isActive,
    pricePerPerson = pricePerPerson,
    currency = currency,
    termsText = termsText,
    createdAt = createdAt,
    updatedAt = updatedAt,
)