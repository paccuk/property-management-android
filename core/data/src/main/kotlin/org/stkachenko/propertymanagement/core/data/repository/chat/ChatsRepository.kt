package org.stkachenko.propertymanagement.core.data.repository.chat

import kotlinx.coroutines.flow.Flow
import org.stkachenko.propertymanagement.core.data.Syncable
import org.stkachenko.propertymanagement.core.model.data.chat.Chat

interface ChatsRepository : Syncable {
    fun getChatsByUserId(id: String): Flow<List<Chat>>
}