package org.stkachenko.propertymanagement.core.model.data.rental

import java.util.Date
import kotlin.time.Duration

data class RentalOffer(
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