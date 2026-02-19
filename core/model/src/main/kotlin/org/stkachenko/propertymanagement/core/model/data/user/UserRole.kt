package org.stkachenko.propertymanagement.core.model.data.user

enum class UserRole(
    val role: String,
) {
    TENANT("tenant"),
    OWNER("owner"),
}