package org.stkachenko.propertymanagement.core.data.repository.auth

import org.stkachenko.propertymanagement.core.model.data.usersession.UserSessionData

interface AuthRepository {
    suspend fun login(username: String, password: String): Boolean
    suspend fun register(username: String, password: String): Boolean
    suspend fun refreshToken(): Boolean
    suspend fun logout()
}
