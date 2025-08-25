package org.stkachenko.propertymanagement.core.data.model.image

import org.stkachenko.propertymanagement.core.database.model.image.ImageEntity
import org.stkachenko.propertymanagement.core.network.model.image.NetworkImage

fun NetworkImage.asEntity() = ImageEntity(
    id = id,
    url = url,
)