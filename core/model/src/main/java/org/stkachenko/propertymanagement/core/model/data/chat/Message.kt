package org.stkachenko.propertymanagement.core.model.data.chat

data class Message(
    val id: String,
    val content: String,
    val messageType: MessageType,
    val status: MessageStatus,
    val isEdited: Boolean,
    val isDeleted: Boolean,
    val authorId: String,
    val chatId: String,
    val createdAt: Long,
    val updatedAt: Long,
)