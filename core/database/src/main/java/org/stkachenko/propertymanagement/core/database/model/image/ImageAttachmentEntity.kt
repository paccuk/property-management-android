package org.stkachenko.propertymanagement.core.database.model.image

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import org.stkachenko.propertymanagement.core.database.model.chat.ChatEntity
import org.stkachenko.propertymanagement.core.database.model.profile.ProfileEntity
import org.stkachenko.propertymanagement.core.database.model.property.PropertyEntity

@Entity(
    tableName = "image_attachments",
    primaryKeys = ["imageId", "relatedId"],
    foreignKeys = [
        ForeignKey(
            entity = ImageEntity::class,
            parentColumns = ["id"],
            childColumns = ["imageId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = ProfileEntity::class,
            parentColumns = ["userId"],
            childColumns = ["relatedId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = PropertyEntity::class,
            parentColumns = ["id"],
            childColumns = ["relatedId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = ChatEntity::class,
            parentColumns = ["id"],
            childColumns = ["relatedId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["relatedType", "relatedId"]),
        Index("imageId"),
        Index("relatedId"),
    ],
)
data class ImageAttachmentEntity(
    val imageId: String,
    val relatedType: String,
    val relatedId: String,
    val imageType: String,
    val position: Int,
    val createdAt: Long,
    val updatedAt: Long,
)