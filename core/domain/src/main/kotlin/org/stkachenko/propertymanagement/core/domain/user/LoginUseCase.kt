package org.stkachenko.propertymanagement.core.domain.user

import org.stkachenko.propertymanagement.core.data.repository.auth.AuthRepository
import org.stkachenko.propertymanagement.core.data.repository.user.UserRepository
import org.stkachenko.propertymanagement.core.datastore.user_preferences.PmPreferencesDataSource
import org.stkachenko.propertymanagement.core.model.data.usersession.UserSessionData
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val pmPreferencesDataSource: PmPreferencesDataSource,
) {
    suspend operator fun invoke(
        username: String,
        password: String,
    ) {
        val isSuccessful = authRepository.login(username, password)

        if (!isSuccessful) {
            throw Exception("Login failed")
        }

        val user = userRepository.getUserByToken()
        pmPreferencesDataSource.updateUserSessionData(
            UserSessionData(
                userId = user.id,
                userRole = user.role,
                isLoggedIn = true,
            )
        )
    }
}