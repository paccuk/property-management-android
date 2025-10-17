package org.stkachenko.propertymanagement.core.network.model.user

import kotlinx.serialization.Serializable

@Serializable
data class NetworkUser(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val role: String,
    val avatarImageUrl: String,
    val createdAt: Long,
    val updatedAt: Long,
)