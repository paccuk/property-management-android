package org.stkachenko.propertymanagement.core.database.model.chat

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import org.stkachenko.propertymanagement.core.model.data.chat.Message
import org.stkachenko.propertymanagement.core.model.data.chat.MessageStatus
import org.stkachenko.propertymanagement.core.model.data.chat.MessageType

@Entity(
    tableName = "messages",
    foreignKeys = [
        ForeignKey(
            entity = ChatParticipantEntity::class,
            parentColumns = ["userId", "chatId"],
            childColumns = ["authorId", "chatId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = ChatEntity::class,
            parentColumns = ["id"],
            childColumns = ["chatId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["authorId", "chatId"]),
        Index("chatId"),
    ]
)
data class MessageEntity(
    @PrimaryKey
    val id: String,
    val content: String,
    val messageType: String,
    val status: String,
    val isEdited: Boolean,
    val isDeleted: Boolean,
    val authorId: String,
    val chatId: String,
    val createdAt: Long,
    val updatedAt: Long,
)

fun MessageEntity.asExternalModel() = Message(
    id = id,
    content = content,
    messageType = MessageType.fromString(messageType),
    status = MessageStatus.fromString(status),
    isEdited = isEdited,
    isDeleted = isDeleted,
    authorId = authorId,
    chatId = chatId,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun MessageType.Companion.fromString(type: String): MessageType =
    when (type) {
        "text" -> MessageType.TEXT
        "image" -> MessageType.IMAGE
        "video" -> MessageType.VIDEO
        "audio" -> MessageType.AUDIO
        "file" -> MessageType.FILE
        "location" -> MessageType.LOCATION
        "sticker" -> MessageType.STICKER
        "contact" -> MessageType.CONTACT
        else -> throw IllegalArgumentException("Unknown message type: $type")

    }

fun MessageStatus.Companion.fromString(status: String): MessageStatus =
    when (status) {
        "read" -> MessageStatus.READ
        "unread" -> MessageStatus.UNREAD
        "sent" -> MessageStatus.SENT
        "failed" -> MessageStatus.FAILED
        "pending" -> MessageStatus.PENDING
        else -> throw IllegalArgumentException("Unknown message status: $status")
    }