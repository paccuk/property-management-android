package org.stkachenko.propertymanagement.core.network.model.rental

import kotlinx.serialization.Serializable

@Serializable
data class NetworkRentalInvite(
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
