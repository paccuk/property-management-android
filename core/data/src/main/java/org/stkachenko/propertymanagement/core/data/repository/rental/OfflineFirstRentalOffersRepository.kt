package org.stkachenko.propertymanagement.core.data.repository.rental

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.stkachenko.propertymanagement.core.data.Synchronizer
import org.stkachenko.propertymanagement.core.data.changeListSync
import org.stkachenko.propertymanagement.core.data.model.rental.asEntity
import org.stkachenko.propertymanagement.core.database.dao.rental.RentalOfferDao
import org.stkachenko.propertymanagement.core.database.model.rental.RentalOfferEntity
import org.stkachenko.propertymanagement.core.database.model.rental.asExternalModel
import org.stkachenko.propertymanagement.core.datastore.ChangeListVersions
import org.stkachenko.propertymanagement.core.model.data.rental.RentalOffer
import org.stkachenko.propertymanagement.core.network.ProtectedNetworkDataSource
import org.stkachenko.propertymanagement.core.network.model.rental.NetworkRentalOffer

internal class OfflineFirstRentalOffersRepository(
    private val rentalOfferDao: RentalOfferDao,
    private val network: ProtectedNetworkDataSource,
) : RentalOffersRepository {

    override fun getRentalOfferEntitiesByOwnerId(id: String): Flow<List<RentalOffer>> =
        rentalOfferDao.getRentalOfferEntitiesByOwnerId(id)
            .map { it.map(RentalOfferEntity::asExternalModel) }

    override fun getRentalOfferEntitiesByPropertyId(id: String): Flow<List<RentalOffer>> =
        rentalOfferDao.getRentalOfferEntitiesByPropertyId(id)
            .map { it.map(RentalOfferEntity::asExternalModel) }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean =
        synchronizer.changeListSync(
            versionReader = ChangeListVersions::rentalOfferVersion,
            changeListFetcher = { currentVersion ->
                network.getRentalOfferChangeList(after = currentVersion)
            },
            versionUpdater = { latestVersion ->
                copy(rentalOfferVersion = latestVersion)
            },
            modelDeleter = rentalOfferDao::deleteRentalOffers,
            modelUpdater = { changedIds ->
                val networkRentalOffers = network.getRentalOffers(ids = changedIds)
                rentalOfferDao.upsertRentalOffers(
                    entities = networkRentalOffers.map(NetworkRentalOffer::asEntity),
                )
            },
        )
}