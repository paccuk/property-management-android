package org.stkachenko.propertymanagement.core.data.model.chat

import org.stkachenko.propertymanagement.core.database.model.chat.ChatEntity
import org.stkachenko.propertymanagement.core.network.model.chat.NetworkChat

fun NetworkChat.asEntity() = ChatEntity(
    id = id,
    name = name,
    unreadCount = unreadCount,
    isGroup = isGroup,
    isArchived = isArchived,
    isMuted = isMuted,
    isPinned = isPinned,
    isDeleted = isDeleted,
    isBlocked = isBlocked,
    lastMessageId = lastMessageId,
)