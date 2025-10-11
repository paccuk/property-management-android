package org.stkachenko.propertymanagement.core.data.repository.chat

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.stkachenko.propertymanagement.core.data.Synchronizer
import org.stkachenko.propertymanagement.core.data.changeListSync
import org.stkachenko.propertymanagement.core.data.model.chat.asEntity
import org.stkachenko.propertymanagement.core.database.dao.chat.ChatParticipantDao
import org.stkachenko.propertymanagement.core.database.model.chat.ChatParticipantEntity
import org.stkachenko.propertymanagement.core.database.model.chat.asExternalModel
import org.stkachenko.propertymanagement.core.datastore.ChangeListVersions
import org.stkachenko.propertymanagement.core.model.data.chat.ChatParticipant
import org.stkachenko.propertymanagement.core.network.ProtectedNetworkDataSource
import org.stkachenko.propertymanagement.core.network.model.chat.NetworkChatParticipant
import javax.inject.Inject

internal class OfflineFirstChatParticipantsRepository @Inject constructor(
    private val chatParticipantDao: ChatParticipantDao,
    private val network: ProtectedNetworkDataSource,
) : ChatParticipantsRepository {

    override fun getChatParticipantsByChatId(chatId: String): Flow<List<ChatParticipant>> =
        chatParticipantDao.getChatParticipantsByChatId(chatId)
            .map { it.map(ChatParticipantEntity::asExternalModel) }


    override suspend fun syncWith(synchronizer: Synchronizer): Boolean =
        synchronizer.changeListSync(
            versionReader = ChangeListVersions::chatParticipantVersion,
            changeListFetcher = { currentVersion ->
                network.getChatParticipantChangeList(after = currentVersion)
            },
            versionUpdater = { latestVersion ->
                copy(chatParticipantVersion = latestVersion)
            },
            modelDeleter = chatParticipantDao::deleteChatParticipantsByChatIds,
            modelUpdater = { changedIds ->
                val networkChatParticipants = network.getChatParticipants(ids = changedIds)
                chatParticipantDao.upsertChatParticipants(
                    entities = networkChatParticipants.map(NetworkChatParticipant::asEntity)
                )
            }
        )
}