package org.stkachenko.propertymanagement.core.network.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class NetworkAuthTokens(
    val accessToken: String,
    val refreshToken: String,
)