package org.stkachenko.propertymanagement.core.database.model.chat

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import org.stkachenko.propertymanagement.core.database.model.user.UserEntity
import org.stkachenko.propertymanagement.core.model.data.chat.ChatParticipant
import org.stkachenko.propertymanagement.core.model.data.chat.ParticipantRole

@Entity(
    tableName = "chat_participants",
    primaryKeys = ["chatId", "userId"],
    foreignKeys = [
        ForeignKey(
            entity = ChatEntity::class,
            parentColumns = ["id"],
            childColumns = ["chatId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index("chatId"),
        Index("userId"),
    ],
)
data class ChatParticipantEntity(
    val chatId: String,
    val userId: String,
    val role: ParticipantRole,
    val isActive: Boolean,
    val lastReadMessageId: String?,
    val joinedAt: Long,
)

fun ChatParticipantEntity.asExternalModel() = ChatParticipant(
    chatId = chatId,
    userId = userId,
    role = role,
    isActive = isActive,
    lastReadMessageId = lastReadMessageId,
    joinedAt = joinedAt,
)
