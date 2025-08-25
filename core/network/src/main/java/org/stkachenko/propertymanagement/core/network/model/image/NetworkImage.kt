package org.stkachenko.propertymanagement.core.network.model.image

import kotlinx.serialization.Serializable

@Serializable
data class NetworkImage(
    val id: String,
    val url: String,
)