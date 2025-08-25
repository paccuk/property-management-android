package org.stkachenko.propertymanagement.core.data.repository.profile

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.stkachenko.propertymanagement.core.data.Synchronizer
import org.stkachenko.propertymanagement.core.data.changeListSync
import org.stkachenko.propertymanagement.core.data.model.profile.asEntity
import org.stkachenko.propertymanagement.core.database.dao.profile.ProfileDao
import org.stkachenko.propertymanagement.core.database.model.profile.asExternalModel
import org.stkachenko.propertymanagement.core.model.data.profile.Profile
import org.stkachenko.propertymanagement.core.network.PmNetworkDataSource
import org.stkachenko.propertymanagement.core.network.model.profile.NetworkProfile
import javax.inject.Inject

internal class OfflineFirstProfilesRepository @Inject constructor(
    private val profileDao: ProfileDao,
    private val network: PmNetworkDataSource,
) : ProfilesRepository {

    override fun getProfileByUserId(id: String): Flow<Profile> =
        profileDao.getProfileEntitiesByUserId(id)
            .map{ it.asExternalModel().first() }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean =
        synchronizer.changeListSync(
            versionReader = { it.profileVersion },
            changeListFetcher = { currentVersion ->
                network.getProfileChangeList(after = currentVersion)
            },
            versionUpdater = { latestVersion ->
                copy(profileVersion = latestVersion)
            },
            modelDeleter = profileDao::deleteProfiles,
            modelUpdater = { changedIds ->
                val networkProfiles = network.getProfiles(ids = changedIds)
                profileDao.upsertProfiles(
                    entities = networkProfiles.map(NetworkProfile::asEntity),
                )
            },
        )
}