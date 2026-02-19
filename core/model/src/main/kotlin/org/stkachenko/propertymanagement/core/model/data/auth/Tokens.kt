package org.stkachenko.propertymanagement.core.model.data.auth

data class Tokens(
    val accessToken: String,
    val refreshToken: String,
)