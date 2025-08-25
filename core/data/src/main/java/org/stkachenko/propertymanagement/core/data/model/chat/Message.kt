package org.stkachenko.propertymanagement.core.data.model.chat

import org.stkachenko.propertymanagement.core.database.model.chat.MessageEntity
import org.stkachenko.propertymanagement.core.network.model.chat.NetworkMessage

fun NetworkMessage.asEntity() = MessageEntity(
    id = id,
    content = content,
    messageType = messageType,
    status = status,
    isEdited = isEdited,
    isDeleted = isDeleted,
    authorId = authorId,
    chatId = chatId,
    createdAt = createdAt,
    updatedAt = updatedAt,
)