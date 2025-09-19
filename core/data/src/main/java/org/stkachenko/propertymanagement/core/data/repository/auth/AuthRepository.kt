package org.stkachenko.propertymanagement.core.data.repository.auth

import org.stkachenko.propertymanagement.core.model.data.user.User

interface AuthRepository {
    fun authenticateUser(username: String, password: String): User?

}
