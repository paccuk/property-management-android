package org.stkachenko.propertymanagement.core.database.model.chat

import androidx.room.Embedded
import org.stkachenko.propertymanagement.core.database.model.image.ImageAttachmentEntity
import org.stkachenko.propertymanagement.core.database.model.image.ImageEntity
import org.stkachenko.propertymanagement.core.model.data.chat.Chat
import org.stkachenko.propertymanagement.core.model.data.image.Image

data class ChatWithImages(
    @Embedded val chat: ChatEntity,
    @Embedded(prefix = "ia_") val imageAttachment: ImageAttachmentEntity?,
    @Embedded(prefix = "i_") val image: ImageEntity?
)

fun List<ChatWithImages>.asExternalModel(): List<Chat> =
    this.groupBy { it.chat.id }
        .map { (_, rows) ->
            val chatEntity = rows.first().chat
            val images = rows.mapNotNull { row ->
                if (row.image != null && row.imageAttachment != null) {
                    Image(
                        id = row.image.id,
                        url = row.image.url,
                        position = row.imageAttachment.position,
                    )
                } else null
            }
            Chat(
                id = chatEntity.id,
                name = chatEntity.name,
                unreadCount = chatEntity.unreadCount,
                isGroup = chatEntity.isGroup,
                isArchived = chatEntity.isArchived,
                isMuted = chatEntity.isMuted,
                isPinned = chatEntity.isPinned,
                isDeleted = chatEntity.isDeleted,
                isBlocked = chatEntity.isBlocked,
                images = images,
                lastMessageId = chatEntity.lastMessageId,
            )
        }