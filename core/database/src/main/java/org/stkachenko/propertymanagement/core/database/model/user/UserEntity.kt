package org.stkachenko.propertymanagement.core.database.model.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.stkachenko.propertymanagement.core.model.data.user.User
import org.stkachenko.propertymanagement.core.model.data.user.UserRole

@Entity(
    tableName = "users",
)
data class UserEntity(
    @PrimaryKey
    val id: String,
    val email: String,
    val phone: String,
    val role: UserRole,
    val isActive: Boolean,
)

fun UserEntity.asExternalModel() = User(
    id = id,
    email = email,
    phone = phone,
    role = role,
    isActive = isActive,
)