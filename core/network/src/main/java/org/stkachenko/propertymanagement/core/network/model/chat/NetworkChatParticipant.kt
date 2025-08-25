package org.stkachenko.propertymanagement.core.network.model.chat

import kotlinx.serialization.Serializable

@Serializable
data class NetworkChatParticipant(
    val chatId: String,
    val userId: String,
    val role: String,
    val isActive: Boolean,
    val lastReadMessageId: String? = null,
    val joinedAt: Long,
)
