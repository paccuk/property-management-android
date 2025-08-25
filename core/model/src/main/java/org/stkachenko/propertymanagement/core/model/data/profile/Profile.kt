package org.stkachenko.propertymanagement.core.model.data.profile

import org.stkachenko.propertymanagement.core.model.data.image.Image

data class Profile(
    val userId: String,
    val firstName: String,
    val lastName: String,
    val avatarImages: List<Image>,
)