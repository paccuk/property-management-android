package org.stkachenko.propertymanagement.core.data.model.user

import org.stkachenko.propertymanagement.core.database.model.user.UserEntity
import org.stkachenko.propertymanagement.core.model.data.user.UserRole
import org.stkachenko.propertymanagement.core.network.model.user.NetworkUser


fun NetworkUser.asEntity() = UserEntity(
    id = id,
    email = email,
    phone = phone,
    isActive = isActive,
    role = UserRole.valueOf(role),
)