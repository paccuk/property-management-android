package org.stkachenko.propertymanagement.core.network.model.image

import kotlinx.serialization.Serializable

@Serializable
data class NetworkImageAttachment(
    val id: String,
    val imageId: String,
    val relatedId: String,
    val relatedType: String,
    val imageType: String,
    val position: Int,
    val createdAt: Long,
    val updatedAt: Long,
)
