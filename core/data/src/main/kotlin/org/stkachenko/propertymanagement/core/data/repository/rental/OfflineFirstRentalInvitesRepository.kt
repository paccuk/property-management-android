package org.stkachenko.propertymanagement.core.data.repository.rental

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.stkachenko.propertymanagement.core.data.model.rental.asEntity
import org.stkachenko.propertymanagement.core.data.model.rental.asNetworkModel
import org.stkachenko.propertymanagement.core.database.dao.rental.RentalInviteDao
import org.stkachenko.propertymanagement.core.database.model.rental.asExternalModel
import org.stkachenko.propertymanagement.core.datastore.user_preferences.PmPreferencesDataSource
import org.stkachenko.propertymanagement.core.model.data.rental.RentalInvite
import org.stkachenko.propertymanagement.core.network.ProtectedNetworkDataSource
import javax.inject.Inject

internal class OfflineFirstRentalInvitesRepository @Inject constructor(
    private val rentalInviteDao: RentalInviteDao,
    private val network: ProtectedNetworkDataSource,
) : RentalInvitesRepository {

    override fun getRentalInviteEntity(id: String): Flow<RentalInvite> =
        rentalInviteDao.getRentalInviteEntity(id).map { it.asExternalModel() }

    override suspend fun syncWith(
        pmPreferences: PmPreferencesDataSource,
        ioDispatcher: CoroutineDispatcher,
    ): Boolean = withContext(ioDispatcher) {
        val lastSync = pmPreferences.getLastRentalInviteSyncTime()
        val localChanged = rentalInviteDao.getRentalInvitesUpdatedAfter(lastSync)
        if (localChanged.isNotEmpty()) {
            val networkModels = localChanged.map { it.asNetworkModel() }
            network.updateRentalInvites(networkModels)
        }
        val updatedFromBackend = network.getRentalInvitesUpdatedAfter(lastSync)
        if (updatedFromBackend.isNotEmpty()) {
            rentalInviteDao.upsertRentalInvites(updatedFromBackend.map { it.asEntity() })
        }
        val newSyncTime =
            (localChanged.map { it.updatedAt } + updatedFromBackend.map { it.updatedAt }).maxOrNull()
                ?: lastSync
        if (newSyncTime > lastSync) {
            pmPreferences.setLastRentalInviteSyncTime(newSyncTime)
        }
        return@withContext true
    }
}