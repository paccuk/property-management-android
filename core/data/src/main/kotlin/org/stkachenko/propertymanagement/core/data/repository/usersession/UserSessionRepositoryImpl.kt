package org.stkachenko.propertymanagement.core.data.repository.usersession

import kotlinx.coroutines.flow.Flow
import org.stkachenko.propertymanagement.core.datastore.user_preferences.PmPreferencesDataSource
import org.stkachenko.propertymanagement.core.model.data.user.UserRole
import org.stkachenko.propertymanagement.core.model.data.usersession.UserSessionData
import javax.inject.Inject

internal class UserSessionRepositoryImpl @Inject constructor(
    private val pmPreferencesDataSource: PmPreferencesDataSource,
) : UserSessionRepository {

    override val userSessionData: Flow<UserSessionData> =
        pmPreferencesDataSource.userSessionData

    override suspend fun updateSessionData(userSessionData: UserSessionData) {
        pmPreferencesDataSource.updateUserSessionData(userSessionData)
    }

    override suspend fun setUserRole(userRole: UserRole) {
        pmPreferencesDataSource.setUserRole(userRole)
    }

    override suspend fun setIsLoggedIn(isLoggedIn: Boolean) {
        pmPreferencesDataSource.setIsLoggedIn(isLoggedIn)
    }

    override suspend fun setUserId(userId: String) {
        pmPreferencesDataSource.setUserId(userId)
    }
}