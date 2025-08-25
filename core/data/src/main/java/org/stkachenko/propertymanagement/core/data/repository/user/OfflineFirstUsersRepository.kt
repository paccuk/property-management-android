package org.stkachenko.propertymanagement.core.data.repository.user

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.stkachenko.propertymanagement.core.data.Synchronizer
import org.stkachenko.propertymanagement.core.data.changeListSync
import org.stkachenko.propertymanagement.core.data.model.user.asEntity
import org.stkachenko.propertymanagement.core.database.dao.user.UserDao
import org.stkachenko.propertymanagement.core.database.model.user.asExternalModel
import org.stkachenko.propertymanagement.core.datastore.ChangeListVersions
import org.stkachenko.propertymanagement.core.model.data.user.User
import org.stkachenko.propertymanagement.core.network.PmNetworkDataSource
import org.stkachenko.propertymanagement.core.network.model.user.NetworkUser
import javax.inject.Inject

internal class OfflineFirstUsersRepository @Inject constructor(
    private val userDao: UserDao,
    private val network: PmNetworkDataSource,
) : UsersRepository {


    override fun getUser(id: String): Flow<User> =
        userDao.getUserEntity(id)
            .map { it.asExternalModel() }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean =
        synchronizer.changeListSync(
            versionReader = ChangeListVersions::userVersion,
            changeListFetcher = { currentVersion ->
                network.getUserChangeList(after = currentVersion)
            },
            versionUpdater = { latestVersion ->
                copy(userVersion = latestVersion)
            },
            modelDeleter = userDao::deleteUsers,
            modelUpdater = { changedIds ->
                val networkUsers = network.getUsers(ids = changedIds)
                userDao.upsertUsers(
                    entities = networkUsers.map(NetworkUser::asEntity),
                )
            },
        )
}