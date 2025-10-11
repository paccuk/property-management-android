package org.stkachenko.propertymanagement.core.storage

interface TokenStorage {

    suspend fun saveTokens(accessToken: String, refreshToken: String)
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun clearTokens()
    suspend fun hasTokens(): Boolean
}
