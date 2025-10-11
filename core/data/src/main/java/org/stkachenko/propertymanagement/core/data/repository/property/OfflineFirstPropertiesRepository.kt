package org.stkachenko.propertymanagement.core.data.repository.property

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.stkachenko.propertymanagement.core.data.Synchronizer
import org.stkachenko.propertymanagement.core.data.changeListSync
import org.stkachenko.propertymanagement.core.data.model.property.asEntity
import org.stkachenko.propertymanagement.core.database.dao.property.PropertyDao
import org.stkachenko.propertymanagement.core.database.model.property.asExternalModel
import org.stkachenko.propertymanagement.core.datastore.ChangeListVersions
import org.stkachenko.propertymanagement.core.model.data.property.Property
import org.stkachenko.propertymanagement.core.network.ProtectedNetworkDataSource
import org.stkachenko.propertymanagement.core.network.model.property.NetworkProperty
import javax.inject.Inject

internal class OfflineFirstPropertiesRepository @Inject constructor(
    private val propertyDao: PropertyDao,
    private val network: ProtectedNetworkDataSource,
) : PropertiesRepository {

    override fun getPropertiesByOwnerId(id: String): Flow<List<Property>> =
        propertyDao.getPropertyEntitiesByOwnerId(id)
            .map { it.asExternalModel() }

    override fun getPropertyById(id: String): Flow<Property?> =
        propertyDao.getPropertyEntityById(id)
            .map { it.asExternalModel().firstOrNull() }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean =
        synchronizer.changeListSync(
            versionReader = ChangeListVersions::propertyVersion,
            changeListFetcher = { currentVersion ->
                network.getPropertyChangeList(after = currentVersion)
            },
            versionUpdater = { latestVersion ->
                copy(propertyVersion = latestVersion)
            },
            modelDeleter = propertyDao::deleteProperties,
            modelUpdater = { changedIds ->
                val networkProperties = network.getProperties(ids = changedIds)
                propertyDao.upsertProperties(
                    entities = networkProperties.map(NetworkProperty::asEntity),
                )
            },
        )
}