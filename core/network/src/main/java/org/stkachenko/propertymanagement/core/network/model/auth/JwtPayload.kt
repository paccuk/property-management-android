package org.stkachenko.propertymanagement.core.network.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class JwtPayload(
    val exp: Long? = null,
)
