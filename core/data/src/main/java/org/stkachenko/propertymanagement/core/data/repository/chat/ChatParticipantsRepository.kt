package org.stkachenko.propertymanagement.core.data.repository.chat

import kotlinx.coroutines.flow.Flow
import org.stkachenko.propertymanagement.core.data.Syncable
import org.stkachenko.propertymanagement.core.model.data.chat.ChatParticipant

interface ChatParticipantsRepository: Syncable {
    fun getChatParticipantsByChatId(chatId: String): Flow<List<ChatParticipant>>
}