package org.stkachenko.propertymanagement.core.model.data.chat

data class ChatParticipant(
    val chatId: String,
    val userId: String,
    val role: ParticipantRole,
    val isActive: Boolean,
    val lastReadMessageId: String?,
    val joinedAt: Long,
)