package org.stkachenko.propertymanagement.core.network

import org.stkachenko.propertymanagement.core.network.model.auth.NetworkAuthTokens

interface AuthNetworkDataSource {

    suspend fun login(username: String, password: String): NetworkAuthTokens
    suspend fun register(username: String, password: String): NetworkAuthTokens
    suspend fun refreshToken(refreshToken: String): NetworkAuthTokens
}