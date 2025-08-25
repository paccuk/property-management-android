package org.stkachenko.propertymanagement.core.data.model.chat

import org.stkachenko.propertymanagement.core.database.model.chat.ChatParticipantEntity
import org.stkachenko.propertymanagement.core.model.data.chat.ParticipantRole
import org.stkachenko.propertymanagement.core.network.model.chat.NetworkChatParticipant

fun NetworkChatParticipant.asEntity() = ChatParticipantEntity(
    chatId = chatId,
    userId = userId,
    role = ParticipantRole.valueOf(role),
    isActive = isActive,
    lastReadMessageId = lastReadMessageId,
    joinedAt = joinedAt,
)