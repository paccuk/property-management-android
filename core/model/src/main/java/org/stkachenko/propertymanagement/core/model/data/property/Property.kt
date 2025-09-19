package org.stkachenko.propertymanagement.core.model.data.property

import org.stkachenko.propertymanagement.core.model.data.image.Image

data class Property(
    val id: String,
    val ownerId: String,
    val price: Double,
    val currency: String,
    val type: String,
    val area: Double,
    val isAvailable: Boolean,
    val address: Map<String, String>,
    val attributes: Map<String, String>,
    val images: List<Image>,
    val createdAt: Long,
    val updatedAt: Long,
)