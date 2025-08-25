package org.stkachenko.propertymanagement.core.database.model.property

import androidx.room.Embedded
import org.stkachenko.propertymanagement.core.database.model.image.ImageAttachmentEntity
import org.stkachenko.propertymanagement.core.database.model.image.ImageEntity
import org.stkachenko.propertymanagement.core.model.data.image.Image
import org.stkachenko.propertymanagement.core.model.data.property.Property

data class PropertyWithImages(
    @Embedded val property: PropertyEntity,
    @Embedded(prefix = "ia_") val imageAttachment: ImageAttachmentEntity?,
    @Embedded(prefix = "i_") val image: ImageEntity?
)

fun List<PropertyWithImages>.asExternalModel(): List<Property> =
    this.groupBy { it.property.id }
        .map { (_, rows) ->
            val propertyEntity = rows.first().property
            val images = rows.mapNotNull { row ->
                if (row.image != null && row.imageAttachment != null) {
                    Image(
                        id = row.image.id,
                        url = row.image.url,
                        position = row.imageAttachment.position,
                    )
                } else null
            }
            Property(
                id = propertyEntity.id,
                ownerId = propertyEntity.ownerId,
                price = propertyEntity.price,
                type = propertyEntity.type,
                area = propertyEntity.area,
                isAvailable = propertyEntity.isAvailable,
                address = propertyEntity.address,
                attributes = propertyEntity.attributes,
                images = images,
                createdAt = propertyEntity.createdAt,
                updatedAt = propertyEntity.updatedAt,
            )
        }