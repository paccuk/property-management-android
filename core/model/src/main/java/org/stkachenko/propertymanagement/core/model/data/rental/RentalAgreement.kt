package org.stkachenko.propertymanagement.core.model.data.rental

import java.util.Date

data class RentalAgreement(
    val id: String,
    val offerId: String,
    val tenantId: String,
    val startDate: Date,
    val endDate: Date,
    val status: String,
    val createdAt: Long,
    val updatedAt: Long,
)