package org.stkachenko.propertymanagement.core.database.model.chat

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.stkachenko.propertymanagement.core.model.data.chat.Chat

@Entity(
    tableName = "chats",
)
data class ChatEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val unreadCount: Int,
    val isGroup: Boolean,
    val isArchived: Boolean,
    val isMuted: Boolean,
    val isPinned: Boolean,
    val isDeleted: Boolean,
    val isBlocked: Boolean,
    val lastMessageId: String?,
)