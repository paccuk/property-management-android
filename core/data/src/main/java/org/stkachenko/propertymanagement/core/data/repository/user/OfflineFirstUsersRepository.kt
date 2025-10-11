package org.stkachenko.propertymanagement.core.data.repository.user

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.stkachenko.propertymanagement.core.data.Synchronizer
import org.stkachenko.propertymanagement.core.data.changeListSync
import org.stkachenko.propertymanagement.core.data.model.user.asEntity
import org.stkachenko.propertymanagement.core.database.dao.user.UserDao
import org.stkachenko.propertymanagement.core.database.model.user.UserEntity
import org.stkachenko.propertymanagement.core.database.model.user.asExternalModel
import org.stkachenko.propertymanagement.core.datastore.ChangeListVersions
import org.stkachenko.propertymanagement.core.model.data.user.User
import org.stkachenko.propertymanagement.core.model.data.user.UserRole
import org.stkachenko.propertymanagement.core.network.ProtectedNetworkDataSource
import org.stkachenko.propertymanagement.core.network.model.user.NetworkUser
import java.util.UUID
import javax.inject.Inject

internal class OfflineFirstUsersRepository @Inject constructor(
    private val userDao: UserDao,
    private val network: ProtectedNetworkDataSource,
) : UserRepository {


    override fun getUser(id: String): Flow<User> =
        userDao.getUserEntityById(id)
            .map { it.asExternalModel() }

    override suspend fun completeUserProfile(
        firstName: String,
        lastName: String,
        phone: String,
        role: String,
        avatarImageUrl: String?,
    ) {
        val userEntity = UserEntity(
            id = UUID.randomUUID().toString(),
            firstName = firstName,
            lastName = lastName,
            email = "",
            phone = phone,
            role = UserRole.valueOf(role),
            avatarImages = listOfNotNull(avatarImageUrl),
        )

        userDao.upsertUsers(listOf(userEntity))
    }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean {

        val upstreamSuccess = syncUpstream()

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

    private suspend fun syncUpstream(): Boolean {
        val pendingUsers = userDao.getPendingSyncUsers()

        if (pendingUsers.isEmpty()) {
            return true
        }

        return try {
            pendingUsers.forEach { userEntity ->
                val created
            }
        }
    }
}