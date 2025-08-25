package org.stkachenko.propertymanagement.core.database.model.image

import androidx.room.Embedded
import org.stkachenko.propertymanagement.core.model.data.image.Image

data class ImageWithAttachment(
    @Embedded val image: ImageEntity,
    @Embedded val attachment: ImageAttachmentEntity,
)

fun ImageWithAttachment.asExternalModel() = Image(
    id = image.id,
    url = image.url,
    position = attachment.position,
)