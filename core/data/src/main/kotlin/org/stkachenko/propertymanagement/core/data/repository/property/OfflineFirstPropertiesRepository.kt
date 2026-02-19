package org.stkachenko.propertymanagement.core.data.repository.property

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.stkachenko.propertymanagement.core.data.model.property.asEntity
import org.stkachenko.propertymanagement.core.data.model.property.asNetworkModel
import org.stkachenko.propertymanagement.core.database.dao.property.PropertyDao
import org.stkachenko.propertymanagement.core.database.model.property.PropertyEntity
import org.stkachenko.propertymanagement.core.database.model.property.asExternalModel
import org.stkachenko.propertymanagement.core.datastore.user_preferences.PmPreferencesDataSource
import org.stkachenko.propertymanagement.core.model.data.property.Property
import org.stkachenko.propertymanagement.core.network.ProtectedNetworkDataSource
import javax.inject.Inject

internal class OfflineFirstPropertiesRepository @Inject constructor(
    private val propertyDao: PropertyDao,
    private val network: ProtectedNetworkDataSource,
) : PropertiesRepository {

    override fun getPropertiesByOwnerId(ownerId: String): Flow<List<Property>> =
        propertyDao.getPropertyEntitiesByOwnerId(ownerId)
            .map { it.map(PropertyEntity::asExternalModel) }

    override fun getPropertyById(id: String): Flow<Property?> =
        propertyDao.getPropertyEntityById(id)
            .map { it?.asExternalModel() }

    override suspend fun syncWith(
        pmPreferences: PmPreferencesDataSource,
        ioDispatcher: CoroutineDispatcher,
    ): Boolean =
        withContext(ioDispatcher) {
            val lastSyncTime = pmPreferences.getLastPropertySyncTime()

            val localChanged = propertyDao.getPropertiesUpdatedAfter(lastSyncTime)
            if (localChanged.isNotEmpty()) {
                val networkModels = localChanged.map { it.asNetworkModel() }
                network.updateProperties(networkModels)
            }
            val updatedFromBackend = network.getPropertiesUpdatedAfter(lastSyncTime)
            if (updatedFromBackend.isNotEmpty()) {
                propertyDao.upsertProperties(updatedFromBackend.map { it.asEntity() })
            }
            val newSyncTime =
                (localChanged.map { it.updatedAt } + updatedFromBackend.map { it.updatedAt }).maxOrNull()
                    ?: lastSyncTime
            if (newSyncTime > lastSyncTime) {
                pmPreferences.setLastPropertySyncTime(newSyncTime)
            }
            return@withContext true
        }
}