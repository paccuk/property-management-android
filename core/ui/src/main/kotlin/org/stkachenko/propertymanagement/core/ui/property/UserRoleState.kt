package org.stkachenko.propertymanagement.core.ui.property

import org.stkachenko.propertymanagement.core.model.data.user.UserRole

sealed interface UserRoleState {
    data class Success(val userRole: UserRole) : UserRoleState
    data object Loading : UserRoleState
    data object Error : UserRoleState
}