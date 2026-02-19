package org.stkachenko.propertymanagement.core.model.data.usersession

import org.stkachenko.propertymanagement.core.model.data.user.UserRole

data class UserSessionData(
    val userRole: UserRole,
    val isLoggedIn: Boolean,
    val userId: String,
)
