package org.stkachenko.propertymanagement.core.network.model.user

import kotlinx.serialization.Serializable

@Serializable
data class NetworkUser(
    val id: String,
    val role: String,
    val email: String,
    val phone: String,
    val isActive: Boolean,
)