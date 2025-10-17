package org.stkachenko.propertymanagement.core.data.model.user

import org.stkachenko.propertymanagement.core.database.model.user.UserEntity
import org.stkachenko.propertymanagement.core.model.data.user.UserRole
import org.stkachenko.propertymanagement.core.network.model.user.NetworkUser


fun NetworkUser.asEntity() = UserEntity(
    id = id,
    firstName = firstName,
    lastName = lastName,
    email = email,
    phone = phone,
    role = UserRole.valueOf(role),
    avatarImageUrl = avatarImageUrl,
    updatedAt = updatedAt,
    createdAt = createdAt,
)

fun UserEntity.asNetworkModel() = NetworkUser(
    id = id,
    firstName = firstName,
    lastName = lastName,
    email = email,
    phone = phone,
    role = role.name,
    avatarImageUrl = avatarImageUrl,
    updatedAt = updatedAt,
    createdAt = createdAt,
)