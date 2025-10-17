package org.stkachenko.propertymanagement.core.data.repository.user

import kotlinx.coroutines.flow.Flow
import org.stkachenko.propertymanagement.core.data.Syncable
import org.stkachenko.propertymanagement.core.model.data.user.User

interface UserRepository : Syncable {
    fun getUserById(id: String): Flow<User?>

    suspend fun getUserByToken(): User
    suspend fun completeUserProfile(
        firstName: String,
        lastName: String,
        phone: String,
        role: String,
        avatarImageUrl: String,
    ): User

    suspend fun updateUser(
        firstName: String,
        lastName: String,
        phone: String,
        avatarImageUrl: String,
    ): User
}