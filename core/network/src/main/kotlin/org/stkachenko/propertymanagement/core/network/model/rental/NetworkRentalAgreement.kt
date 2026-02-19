package org.stkachenko.propertymanagement.core.network.model.rental

import kotlinx.serialization.Serializable

@Serializable
data class NetworkRentalAgreement(
    val id: String,
    val offerId: String,
    val tenantId: String,
    val startDate: Long,
    val endDate: Long,
    val status: String,
    val createdAt: Long,
    val updatedAt: Long,
)
