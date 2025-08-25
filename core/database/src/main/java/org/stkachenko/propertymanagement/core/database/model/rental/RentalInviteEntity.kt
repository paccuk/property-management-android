package org.stkachenko.propertymanagement.core.database.model.rental

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import org.stkachenko.propertymanagement.core.model.data.rental.RentalInvite

@Entity(
    tableName = "rental_invites",
    foreignKeys = [
        ForeignKey(
            entity = RentalOfferEntity::class,
            parentColumns = ["id"],
            childColumns = ["offerId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index("offerId"),
    ],
)
data class RentalInviteEntity(
    @PrimaryKey
    val id: String,
    val offerId: String,
    val inviteType: String,
    val inviteToken: String,
    val targetContact: String,
    val status: String,
    val usedAt: Long,
    val createdAt: Long,
    val updatedAt: Long,
)

fun RentalInviteEntity.asExternalModel() = RentalInvite(
    id = id,
    offerId = offerId,
    inviteType = inviteType,
    inviteToken = inviteToken,
    targetContact = targetContact,
    status = status,
    usedAt = usedAt,
    createdAt = createdAt,
    updatedAt = updatedAt,
)