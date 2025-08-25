package org.stkachenko.propertymanagement.core.network.model.chat


data class NetworkMessage(
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