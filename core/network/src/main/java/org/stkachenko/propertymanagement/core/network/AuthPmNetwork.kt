package org.stkachenko.propertymanagement.core.network

import org.stkachenko.propertymanagement.core.network.model.auth.AuthResponse

interface AuthPmNetwork {

    suspend fun login(username: String, password: String): AuthResponse
    suspend fun register(username: String, password: String): AuthResponse
    suspend fun refreshToken(refreshToken: String): AuthResponse
}