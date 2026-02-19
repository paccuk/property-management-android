package org.stkachenko.propertymanagement.core.domain.user

import org.stkachenko.propertymanagement.core.data.repository.user.UserRepository
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(
        firstName: String,
        lastName: String,
        phone: String,
        avatarImageUrl: String?,
    ) {
        userRepository.updateUser(
            firstName,
            lastName,
            phone,
            avatarImageUrl ?: "",
        )
    }
}