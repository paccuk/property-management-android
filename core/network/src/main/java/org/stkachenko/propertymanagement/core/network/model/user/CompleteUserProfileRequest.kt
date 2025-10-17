package org.stkachenko.propertymanagement.core.network.model.user

import kotlinx.serialization.Serializable

@Serializable
data class CompleteUserProfileRequest(
    val firstName: String,
    val lastName: String,
    val phone: String,
    val role: String,
    val avatarImageUrl: String,
)