package org.stkachenko.propertymanagement.core.data.repository.chat

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.stkachenko.propertymanagement.core.data.Synchronizer
import org.stkachenko.propertymanagement.core.data.changeListSync
import org.stkachenko.propertymanagement.core.data.model.chat.asEntity
import org.stkachenko.propertymanagement.core.database.dao.chat.MessageDao
import org.stkachenko.propertymanagement.core.database.model.chat.MessageEntity
import org.stkachenko.propertymanagement.core.database.model.chat.asExternalModel
import org.stkachenko.propertymanagement.core.datastore.ChangeListVersions
import org.stkachenko.propertymanagement.core.model.data.chat.Message
import org.stkachenko.propertymanagement.core.network.PmNetworkDataSource
import org.stkachenko.propertymanagement.core.network.model.chat.NetworkMessage
import javax.inject.Inject

internal class OfflineFirstMessagesRepository @Inject constructor(
    private val messageDao: MessageDao,
    private val network: PmNetworkDataSource,
) : MessagesRepository {

    override fun getFilteredMessagesByChatId(
        chatId: String,
        after: Long,
        limit: Int
    ): Flow<List<Message>> =
        messageDao.getFilteredMessagesByChatId(chatId, after, limit)
            .map { it.map(MessageEntity::asExternalModel) }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean =
        synchronizer.changeListSync(
            versionReader = ChangeListVersions::messageVersion,
            changeListFetcher = { currentVersion ->
                network.getMessageChangeList(after = currentVersion)
            },
            versionUpdater = { latestVersion ->
                copy(messageVersion = latestVersion)
            },
            modelDeleter = messageDao::deleteMessages,
            modelUpdater = { changedIds ->
                val networkMessages = network.getMessages(ids = changedIds)
                messageDao.upsertMessages(
                    entities = networkMessages.map(NetworkMessage::asEntity)
                )
            }
        )
}