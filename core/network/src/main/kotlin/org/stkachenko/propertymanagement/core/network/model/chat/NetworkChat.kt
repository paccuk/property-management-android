package org.stkachenko.propertymanagement.core.network.model.chat

import kotlinx.serialization.Serializable

@Serializable
data class NetworkChat(
    val id: String,
    val name: String,
    val unreadCount: Int,
    val isGroup: Boolean,
    val isArchived: Boolean,
    val isMuted: Boolean,
    val isPinned: Boolean,
    val isDeleted: Boolean,
    val isBlocked: Boolean,
    val lastMessageId: String? = null,
)