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
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val role: UserRole,
    val avatarImages: List<String>,
    val isPendingSync: Boolean = false
)

fun UserEntity.asExternalModel() = User(
    id = id,
    firstName = firstName,
    lastName = lastName,
    email = email,
    phone = phone,
    role = role,
    avatarImages = avatarImages,
)