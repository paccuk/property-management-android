package org.stkachenko.propertymanagement.core.data.repository.user

import kotlinx.coroutines.flow.Flow
import org.stkachenko.propertymanagement.core.data.Syncable
import org.stkachenko.propertymanagement.core.model.data.user.User

interface UsersRepository: Syncable {
    fun getUser(id: String): Flow<User>
}