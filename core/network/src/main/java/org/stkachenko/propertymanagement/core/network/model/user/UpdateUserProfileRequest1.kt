package org.stkachenko.propertymanagement.core.network.model.user

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserProfileRequest(
    val firstName: String,
    val lastName: String,
    val phone: String,
    val avatarImageUrl: String,
)