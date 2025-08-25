package org.stkachenko.propertymanagement.core.network.model.profile

import kotlinx.serialization.Serializable

@Serializable
data class NetworkProfile(
    val userId: String,
    val firstName: String,
    val lastName: String,
)