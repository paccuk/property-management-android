package org.stkachenko.propertymanagement.core.network.model.rental

import kotlinx.serialization.Serializable

@Serializable
data class NetworkRentalOffer(
    val id: String,
    val propertyId: String,
    val ownerId: String,
    val availableFrom: Long,
    val availableTo: Long,
    val minDuration: Long,
    val maxDuration: Long,
    val naxTenants: Int,
    val isActive: Boolean,
    val pricePerPerson: Double,
    val currency: String,
    val termsText: String,
    val createdAt: Long,
    val updatedAt: Long,
)
