package org.stkachenko.propertymanagement.core.data.repository.user

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.stkachenko.propertymanagement.core.data.model.user.asEntity
import org.stkachenko.propertymanagement.core.data.model.user.asNetworkModel
import org.stkachenko.propertymanagement.core.database.dao.user.UserDao
import org.stkachenko.propertymanagement.core.database.model.user.asExternalModel
import org.stkachenko.propertymanagement.core.datastore.user_preferences.PmPreferencesDataSource
import org.stkachenko.propertymanagement.core.model.data.user.User
import org.stkachenko.propertymanagement.core.network.ProtectedNetworkDataSource
import org.stkachenko.propertymanagement.core.network.model.user.CompleteUserProfileRequest
import org.stkachenko.propertymanagement.core.network.model.user.UpdateUserProfileRequest
import javax.inject.Inject

internal class OfflineFirstUsersRepository @Inject constructor(
    private val userDao: UserDao,
    private val network: ProtectedNetworkDataSource,
) : UserRepository {

    override fun getUserById(id: String): Flow<User?> =
        userDao.getUserEntityById(id)
            .map { it?.asExternalModel() }

    override suspend fun getUserByToken(): User {
        val networkUser = network.getUserByToken()
        val userEntity = networkUser.asEntity()
        userDao.upsertUsers(listOf(userEntity))
        return userEntity.asExternalModel()
    }

    override suspend fun completeUserProfile(
        firstName: String,
        lastName: String,
        phone: String,
        role: String,
        avatarImageUrl: String,
    ): User {

        val networkUser = network.completeUserProfile(
            CompleteUserProfileRequest(
                firstName = firstName,
                lastName = lastName,
                phone = phone,
                role = role,
                avatarImageUrl = avatarImageUrl,
            )
        )

        val userEntity = networkUser.asEntity()
        userDao.upsertUsers(listOf(userEntity))
        return userEntity.asExternalModel()
    }

    override suspend fun updateUser(
        firstName: String,
        lastName: String,
        phone: String,
        avatarImageUrl: String,
    ): User {
        val networkUser = network.updateUser(
            UpdateUserProfileRequest(
                firstName = firstName,
                lastName = lastName,
                phone = phone,
                avatarImageUrl = avatarImageUrl,
            )
        )

        val userEntity = networkUser.asEntity()
        userDao.upsertUsers(listOf(userEntity))
        return userEntity.asExternalModel()
    }

    override suspend fun syncWith(
        pmPreferences: PmPreferencesDataSource,
        ioDispatcher: CoroutineDispatcher,
    ): Boolean = withContext(ioDispatcher) {
        val lastSync = pmPreferences.getLastUserSyncTime()

        val localChanged = userDao.getUsersUpdatedAfter(lastSync)
        if (localChanged.isNotEmpty()) {
            val networkModels = localChanged.map { it.asNetworkModel() }
            network.updateUsers(networkModels)
        }

        val updatedFromBackend = network.getUsersUpdatedAfter(lastSync)
        if (updatedFromBackend.isNotEmpty()) {
            userDao.upsertUsers(updatedFromBackend.map { it.asEntity() })
        }

        val newSyncTime =
            (localChanged.map { it.updatedAt } + updatedFromBackend.map { it.updatedAt }).maxOrNull()
                ?: lastSync

        if (newSyncTime > lastSync) {
            pmPreferences.setLastUserSyncTime(newSyncTime)
        }

        return@withContext true
    }
}