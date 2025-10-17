package org.stkachenko.propertymanagement.core.domain.user

import org.stkachenko.propertymanagement.core.data.repository.user.UserRepository
import org.stkachenko.propertymanagement.core.datastore.user_preferences.PmPreferencesDataSource
import org.stkachenko.propertymanagement.core.model.data.usersession.UserSessionData
import javax.inject.Inject

class CompleteUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val pmPreferencesDataSource: PmPreferencesDataSource,
) {
    suspend operator fun invoke(
        firstName: String,
        lastName: String,
        phone: String,
        role: String,
        avatarImageUrl: String?,
    ) {
        val user = userRepository.completeUserProfile(

            firstName,
            lastName,
            phone,
            role,
            avatarImageUrl ?: "",
        )

        pmPreferencesDataSource.updateUserSessionData(
            UserSessionData(
                userId = user.id,
                userRole = user.role,
                isLoggedIn = true,
            )
        )
    }
}