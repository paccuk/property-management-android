package org.stkachenko.propertymanagement.core.domain.user

import kotlinx.coroutines.flow.Flow
import org.stkachenko.propertymanagement.core.data.repository.user.UserRepository
import org.stkachenko.propertymanagement.core.model.data.user.User
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(
        id: String,
    ): Flow<User?> {
        return userRepository.getUserById(id)

    }
}