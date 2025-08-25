package org.stkachenko.propertymanagement.core.model.data.user

data class User(
    val id: String,
    val email: String,
    val phone: String,
    val role: UserRole,
    val isActive: Boolean,
    )