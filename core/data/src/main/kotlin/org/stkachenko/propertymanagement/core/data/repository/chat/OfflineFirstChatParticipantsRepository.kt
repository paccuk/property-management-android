package org.stkachenko.propertymanagement.core.data.repository.chat

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.stkachenko.propertymanagement.core.database.dao.chat.ChatParticipantDao
import org.stkachenko.propertymanagement.core.database.model.chat.ChatParticipantEntity
import org.stkachenko.propertymanagement.core.database.model.chat.asExternalModel
import org.stkachenko.propertymanagement.core.datastore.user_preferences.PmPreferencesDataSource
import org.stkachenko.propertymanagement.core.model.data.chat.ChatParticipant
import org.stkachenko.propertymanagement.core.network.ProtectedNetworkDataSource
import javax.inject.Inject

internal class OfflineFirstChatParticipantsRepository @Inject constructor(
    private val chatParticipantDao: ChatParticipantDao,
    private val network: ProtectedNetworkDataSource,
) : ChatParticipantsRepository {

    override fun getChatParticipantsByChatId(chatId: String): Flow<List<ChatParticipant>> =
        chatParticipantDao.getChatParticipantsByChatId(chatId)
            .map { it.map(ChatParticipantEntity::asExternalModel) }

    override suspend fun syncWith(
        pmPreferences: PmPreferencesDataSource,
        ioDispatcher: CoroutineDispatcher,
    ): Boolean {
        TODO("Not yet implemented")
    }
}