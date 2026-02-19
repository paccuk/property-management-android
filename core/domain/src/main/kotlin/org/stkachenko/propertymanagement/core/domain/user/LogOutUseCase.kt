package org.stkachenko.propertymanagement.core.domain.user

import org.stkachenko.propertymanagement.core.data.repository.auth.AuthRepository
import org.stkachenko.propertymanagement.core.datastore.user_preferences.PmPreferencesDataSource
import javax.inject.Inject

class LogOutUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val pmPreferencesDataSource: PmPreferencesDataSource,
) {
    suspend operator fun invoke() {
        authRepository.logout()
        pmPreferencesDataSource.clearUserSessionData()
    }
}