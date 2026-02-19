package org.stkachenko.propertymanagement.core.database.util

import androidx.room.TypeConverter
import org.stkachenko.propertymanagement.core.model.data.user.UserRole

internal class UserRoleConverter {

    @TypeConverter
    fun userRoleToString(role: UserRole?): String? =
        role?.let {
            when (it) {
                UserRole.TENANT -> "tenant"
                UserRole.OWNER -> "owner"
            }
        }

    @TypeConverter
    fun stringToUserRole(value: String?): UserRole? =
        value?.let {
            when (it) {
                "tenant" -> UserRole.TENANT
                "owner" -> UserRole.OWNER
                else -> null
            }
        }
}