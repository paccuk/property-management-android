package org.stkachenko.propertymanagement.core.data.repository.usersession

import kotlinx.coroutines.flow.Flow
import org.stkachenko.propertymanagement.core.model.data.user.UserRole

interface UserSessionRepository {
    val userRole: Flow<UserRole>

}