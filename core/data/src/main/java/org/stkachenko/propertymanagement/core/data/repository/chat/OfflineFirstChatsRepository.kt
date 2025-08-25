package org.stkachenko.propertymanagement.core.data.repository.chat

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.stkachenko.propertymanagement.core.data.Synchronizer
import org.stkachenko.propertymanagement.core.data.changeListSync
import org.stkachenko.propertymanagement.core.data.model.chat.asEntity
import org.stkachenko.propertymanagement.core.database.dao.chat.ChatDao
import org.stkachenko.propertymanagement.core.database.model.chat.asExternalModel
import org.stkachenko.propertymanagement.core.datastore.ChangeListVersions
import org.stkachenko.propertymanagement.core.model.data.chat.Chat
import org.stkachenko.propertymanagement.core.network.PmNetworkDataSource
import org.stkachenko.propertymanagement.core.network.model.chat.NetworkChat
import javax.inject.Inject

internal class OfflineFirstChatsRepository @Inject constructor(
    private val chatsDao: ChatDao,
    private val network: PmNetworkDataSource,
) : ChatsRepository {

    override fun getChatsByUserId(id: String): Flow<List<Chat>> =
        chatsDao.getChatEntitiesByUserId(id)
            .map { it.asExternalModel() }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean =
        synchronizer.changeListSync(
            versionReader = ChangeListVersions::chatVersion,
            changeListFetcher = { currentVersion ->
                network.getChatChangeList(after = currentVersion)
            },
            versionUpdater = { latestVersion ->
                copy(chatVersion = latestVersion)
            },
            modelDeleter = chatsDao::deleteChats,
            modelUpdater = { changedIds ->
                val networkChats = network.getChats(ids = changedIds)
                chatsDao.upsertChats(
                    entities = networkChats.map(NetworkChat::asEntity),
                )
            },
        )
}