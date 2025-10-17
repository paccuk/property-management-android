package org.stkachenko.propertymanagement.core.data.repository.chat

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.stkachenko.propertymanagement.core.data.model.chat.asEntity
import org.stkachenko.propertymanagement.core.database.dao.chat.MessageDao
import org.stkachenko.propertymanagement.core.database.model.chat.MessageEntity
import org.stkachenko.propertymanagement.core.database.model.chat.asExternalModel
import org.stkachenko.propertymanagement.core.datastore.user_preferences.PmPreferencesDataSource
import org.stkachenko.propertymanagement.core.model.data.chat.Message
import org.stkachenko.propertymanagement.core.network.ProtectedNetworkDataSource
import org.stkachenko.propertymanagement.core.network.model.chat.NetworkMessage
import javax.inject.Inject

internal class OfflineFirstMessagesRepository @Inject constructor(
    private val messageDao: MessageDao,
    private val network: ProtectedNetworkDataSource,
) : MessagesRepository {

    override fun getFilteredMessagesByChatId(
        chatId: String,
        after: Long,
        limit: Int,
    ): Flow<List<Message>> =
        messageDao.getFilteredMessagesByChatId(chatId, after, limit)
            .map { it.map(MessageEntity::asExternalModel) }

    override suspend fun syncWith(
        pmPreferences: PmPreferencesDataSource,
        ioDispatcher: CoroutineDispatcher,
    ): Boolean {
        TODO("Not yet implemented")
    }
}