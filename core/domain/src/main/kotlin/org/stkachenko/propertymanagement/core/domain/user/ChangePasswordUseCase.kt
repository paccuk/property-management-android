package org.stkachenko.propertymanagement.core.domain.user

import org.stkachenko.propertymanagement.core.data.repository.user.UserRepository
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String,
    ) {
        if (newPassword != confirmNewPassword) {
            throw Exception("New password and confirmation do not match")
        }

        userRepository.changePassword(
            currentPassword = currentPassword,
            newPassword = newPassword,
        )
    }
}