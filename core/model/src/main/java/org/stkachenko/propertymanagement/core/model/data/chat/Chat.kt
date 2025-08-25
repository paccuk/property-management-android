package org.stkachenko.propertymanagement.core.model.data.chat

import org.stkachenko.propertymanagement.core.model.data.image.Image

data class Chat(
    val id: String,
    val name: String,
    val unreadCount: Int,
    val isGroup: Boolean,
    val isArchived: Boolean,
    val isMuted: Boolean,
    val isPinned: Boolean,
    val isDeleted: Boolean,
    val isBlocked: Boolean,
    val images: List<Image>,
    val lastMessageId: String?,
)