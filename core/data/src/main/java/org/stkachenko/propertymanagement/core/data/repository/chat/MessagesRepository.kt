package org.stkachenko.propertymanagement.core.data.repository.chat

import kotlinx.coroutines.flow.Flow
import org.stkachenko.propertymanagement.core.data.Syncable
import org.stkachenko.propertymanagement.core.model.data.chat.Message

interface MessagesRepository : Syncable {
    fun getFilteredMessagesByChatId(chatId: String, after: Long, limit: Int): Flow<List<Message>>
}