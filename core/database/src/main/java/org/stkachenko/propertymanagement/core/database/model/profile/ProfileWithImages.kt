package org.stkachenko.propertymanagement.core.database.model.profile

import androidx.room.Embedded
import org.stkachenko.propertymanagement.core.database.model.image.ImageAttachmentEntity
import org.stkachenko.propertymanagement.core.database.model.image.ImageEntity
import org.stkachenko.propertymanagement.core.model.data.image.Image
import org.stkachenko.propertymanagement.core.model.data.profile.Profile

data class ProfileWithImages(
    @Embedded val profile: ProfileEntity,
    @Embedded val imageAttachment: ImageAttachmentEntity?,
    @Embedded val image: ImageEntity?
)

fun List<ProfileWithImages>.asExternalModel(): List<Profile> =
    this.groupBy { it.profile.userId }
        .map { (_, rows) ->
            val profileEntity = rows.first().profile
            val images = rows.mapNotNull { row ->
                if (row.image != null && row.imageAttachment != null) {
                    Image(
                        id = row.image.id,
                        url = row.image.url,
                        position = row.imageAttachment.position,
                    )
                } else null
            }
            Profile(
                userId = profileEntity.userId,
                firstName = profileEntity.firstName,
                lastName = profileEntity.lastName,
                avatarImages = images,
            )
        }