package org.stkachenko.propertymanagement.core.data.model.image

import org.stkachenko.propertymanagement.core.database.model.image.ImageAttachmentEntity
import org.stkachenko.propertymanagement.core.network.model.image.NetworkImageAttachment

fun NetworkImageAttachment.asEntity() = ImageAttachmentEntity(
    imageId = imageId,
    relatedId = relatedId,
    relatedType = relatedType,
    imageType = imageType,
    position = position,
    createdAt = createdAt,
    updatedAt = updatedAt,
)