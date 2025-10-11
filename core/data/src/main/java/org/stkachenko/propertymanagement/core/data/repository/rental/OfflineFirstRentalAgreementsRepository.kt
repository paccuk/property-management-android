package org.stkachenko.propertymanagement.core.data.repository.rental

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.stkachenko.propertymanagement.core.data.Synchronizer
import org.stkachenko.propertymanagement.core.data.changeListSync
import org.stkachenko.propertymanagement.core.data.model.rental.asEntity
import org.stkachenko.propertymanagement.core.database.dao.rental.RentalAgreementDao
import org.stkachenko.propertymanagement.core.database.model.rental.RentalAgreementEntity
import org.stkachenko.propertymanagement.core.database.model.rental.asExternalModel
import org.stkachenko.propertymanagement.core.datastore.ChangeListVersions
import org.stkachenko.propertymanagement.core.model.data.rental.RentalAgreement
import org.stkachenko.propertymanagement.core.network.ProtectedNetworkDataSource
import org.stkachenko.propertymanagement.core.network.model.rental.NetworkRentalAgreement
import javax.inject.Inject

internal class OfflineFirstRentalAgreementsRepository @Inject constructor(
    private val rentalAgreementDao: RentalAgreementDao,
    private val network: ProtectedNetworkDataSource,
) : RentalAgreementsRepository {

    override fun getRentalAgreementsByOfferId(id: String): Flow<List<RentalAgreement>> =
        rentalAgreementDao.getRentalAgreementEntitiesByOfferId(id)
            .map { it.map(RentalAgreementEntity::asExternalModel) }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean =
        synchronizer.changeListSync(
            versionReader = ChangeListVersions::rentalAgreementVersion,
            changeListFetcher = { currentVersion ->
                network.getRentalAgreementChangeList(after = currentVersion)
            },
            versionUpdater = { latestVersion ->
                copy(rentalAgreementVersion = latestVersion)
            },
            modelDeleter = rentalAgreementDao::deleteRentalAgreements,
            modelUpdater = { changedIds ->
                val networkRentalAgreements = network.getRentalAgreements(ids = changedIds)
                rentalAgreementDao.upsertRentalAgreements(
                    entities = networkRentalAgreements.map(NetworkRentalAgreement::asEntity),
                )
            },
        )

}