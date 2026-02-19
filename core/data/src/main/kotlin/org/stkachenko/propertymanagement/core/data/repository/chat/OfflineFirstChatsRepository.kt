package org.stkachenko.propertymanagement.core.data.repository.chat

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import org.stkachenko.propertymanagement.core.database.dao.chat.ChatDao
import org.stkachenko.propertymanagement.core.datastore.user_preferences.PmPreferencesDataSource
import org.stkachenko.propertymanagement.core.model.data.chat.Chat
import org.stkachenko.propertymanagement.core.network.ProtectedNetworkDataSource
import javax.inject.Inject

internal class OfflineFirstChatsRepository @Inject constructor(
    private val chatsDao: ChatDao,
    private val network: ProtectedNetworkDataSource,
) : ChatsRepository {

    override fun getChatsByUserId(id: String): Flow<List<Chat>> =
        TODO()

    override suspend fun syncWith(
        pmPreferences: PmPreferencesDataSource,
        ioDispatcher: CoroutineDispatcher,
    ): Boolean {
        TODO("Not yet implemented")
    }
}