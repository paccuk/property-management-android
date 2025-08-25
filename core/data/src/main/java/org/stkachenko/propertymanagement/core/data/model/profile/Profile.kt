package org.stkachenko.propertymanagement.core.data.model.profile

import org.stkachenko.propertymanagement.core.database.model.profile.ProfileEntity
import org.stkachenko.propertymanagement.core.network.model.profile.NetworkProfile

fun NetworkProfile.asEntity() = ProfileEntity(
    userId = userId,
    firstName = firstName,
    lastName = lastName,
)