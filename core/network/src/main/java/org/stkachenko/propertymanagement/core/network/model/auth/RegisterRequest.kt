package org.stkachenko.propertymanagement.core.network.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val username: String,
    val password: String,
)
