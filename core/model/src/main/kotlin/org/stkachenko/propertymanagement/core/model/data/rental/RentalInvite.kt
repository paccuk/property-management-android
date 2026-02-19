package org.stkachenko.propertymanagement.core.model.data.rental

data class RentalInvite(
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