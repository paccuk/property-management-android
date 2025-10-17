package org.stkachenko.propertymanagement.core.data.repository.auth

interface AuthRepository {
    suspend fun login(username: String, password: String): Boolean
    suspend fun register(username: String, password: String): Boolean
    suspend fun refreshToken(): Boolean
    suspend fun logout()
}
