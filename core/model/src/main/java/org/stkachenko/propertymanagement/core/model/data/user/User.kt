package org.stkachenko.propertymanagement.core.model.data.user

data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val role: UserRole,
    val avatarImages: List<String>,
)