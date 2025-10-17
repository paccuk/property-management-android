package org.stkachenko.propertymanagement.core.data.repository.rental

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.stkachenko.propertymanagement.core.data.model.rental.asEntity
import org.stkachenko.propertymanagement.core.data.model.rental.asNetworkModel
import org.stkachenko.propertymanagement.core.database.dao.rental.RentalOfferDao
import org.stkachenko.propertymanagement.core.database.model.rental.RentalOfferEntity
import org.stkachenko.propertymanagement.core.database.model.rental.asExternalModel
import org.stkachenko.propertymanagement.core.datastore.user_preferences.PmPreferencesDataSource
import org.stkachenko.propertymanagement.core.model.data.rental.RentalOffer
import org.stkachenko.propertymanagement.core.network.ProtectedNetworkDataSource

internal class OfflineFirstRentalOffersRepository(
    private val rentalOfferDao: RentalOfferDao,
    private val network: ProtectedNetworkDataSource,
    private val pmPreferences: PmPreferencesDataSource,
) : RentalOffersRepository {

    override fun getRentalOfferEntitiesByOwnerId(id: String): Flow<List<RentalOffer>> =
        rentalOfferDao.getRentalOfferEntitiesByOwnerId(id)
            .map { it.map(RentalOfferEntity::asExternalModel) }

    override fun getRentalOfferEntitiesByPropertyId(id: String): Flow<List<RentalOffer>> =
        rentalOfferDao.getRentalOfferEntitiesByPropertyId(id)
            .map { it.map(RentalOfferEntity::asExternalModel) }

    override suspend fun syncWith(
        pmPreferences: PmPreferencesDataSource,
        ioDispatcher: CoroutineDispatcher,
    ): Boolean = withContext(ioDispatcher) {
        val lastSync = pmPreferences.getLastRentalOfferSyncTime()
        val localChanged = rentalOfferDao.getRentalOffersUpdatedAfter(lastSync)
        if (localChanged.isNotEmpty()) {
            val networkModels = localChanged.map { it.asNetworkModel() }
            network.updateRentalOffers(networkModels)
        }
        val updatedFromBackend = network.getRentalOffersUpdatedAfter(lastSync)
        if (updatedFromBackend.isNotEmpty()) {
            rentalOfferDao.upsertRentalOffers(updatedFromBackend.map { it.asEntity() })
        }
        val newSyncTime = (localChanged.map { it.updatedAt } + updatedFromBackend.map { it.updatedAt }).maxOrNull() ?: lastSync
        if (newSyncTime > lastSync) {
            pmPreferences.setLastRentalOfferSyncTime(newSyncTime)
        }
        return@withContext true
    }
}