package org.stkachenko.propertymanagement.core.domain.user

import org.stkachenko.propertymanagement.core.data.repository.user.UserRepository
import javax.inject.Inject

class CompleteUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    operator fun invoke(
        firstName: String,
        lastName: String,
        phone: String,
        role: String,
        avatarImageUrl: String?,
    ): Boolean =
        userRepository.completeUserProfile(firstName, lastName, phone, role, avatarImageUrl)

}