package org.stkachenko.propertymanagement.core.data.repository.usersession

import kotlinx.coroutines.flow.Flow
import org.stkachenko.propertymanagement.core.model.data.user.UserRole
import org.stkachenko.propertymanagement.core.model.data.usersession.UserSessionData

interface UserSessionRepository {
    val userSessionData: Flow<UserSessionData>

    suspend fun setUserRole(userRole: UserRole)

    suspend fun setIsLoggedIn(isLoggedIn: Boolean)

    suspend fun setUserId(userId: String)
}