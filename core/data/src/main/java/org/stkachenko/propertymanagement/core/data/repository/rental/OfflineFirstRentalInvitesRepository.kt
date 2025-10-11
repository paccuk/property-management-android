package org.stkachenko.propertymanagement.core.data.repository.rental

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.stkachenko.propertymanagement.core.data.Synchronizer
import org.stkachenko.propertymanagement.core.data.changeListSync
import org.stkachenko.propertymanagement.core.data.model.rental.asEntity
import org.stkachenko.propertymanagement.core.database.dao.rental.RentalInviteDao
import org.stkachenko.propertymanagement.core.database.model.rental.asExternalModel
import org.stkachenko.propertymanagement.core.datastore.ChangeListVersions
import org.stkachenko.propertymanagement.core.model.data.rental.RentalInvite
import org.stkachenko.propertymanagement.core.network.ProtectedNetworkDataSource
import org.stkachenko.propertymanagement.core.network.model.rental.NetworkRentalInvite
import javax.inject.Inject

internal class OfflineFirstRentalInvitesRepository @Inject constructor(
    private val rentalInviteDao: RentalInviteDao,
    private val network: ProtectedNetworkDataSource,
) : RentalInvitesRepository {

    override fun getRentalInviteEntity(id: String): Flow<RentalInvite> =
        rentalInviteDao.getRentalInviteEntity(id).map { it.asExternalModel() }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean =
        synchronizer.changeListSync(
            versionReader = ChangeListVersions::rentalInviteVersion,
            changeListFetcher = { currentVersion ->
                network.getRentalInviteChangeList(after = currentVersion)
            },
            versionUpdater = { latestVersion ->
                copy(rentalInviteVersion = latestVersion)
            },
            modelDeleter = rentalInviteDao::deleteRentalInvites,
            modelUpdater = { changedIds ->
                val networkInvites = network.getRentalInvites(ids = changedIds)
                rentalInviteDao.upsertRentalInvites(
                    entities = networkInvites.map(NetworkRentalInvite::asEntity),
                )
            },
        )
}