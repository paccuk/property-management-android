package org.stkachenko.propertymanagement.core.data.repository.rental

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.stkachenko.propertymanagement.core.data.model.rental.asEntity
import org.stkachenko.propertymanagement.core.data.model.rental.asNetworkModel
import org.stkachenko.propertymanagement.core.database.dao.rental.RentalAgreementDao
import org.stkachenko.propertymanagement.core.database.model.rental.RentalAgreementEntity
import org.stkachenko.propertymanagement.core.database.model.rental.asExternalModel
import org.stkachenko.propertymanagement.core.datastore.user_preferences.PmPreferencesDataSource
import org.stkachenko.propertymanagement.core.model.data.rental.RentalAgreement
import org.stkachenko.propertymanagement.core.network.ProtectedNetworkDataSource
import javax.inject.Inject

internal class OfflineFirstRentalAgreementsRepository @Inject constructor(
    private val rentalAgreementDao: RentalAgreementDao,
    private val network: ProtectedNetworkDataSource,
) : RentalAgreementsRepository {

    override fun getRentalAgreementsByOfferId(id: String): Flow<List<RentalAgreement>> =
        rentalAgreementDao.getRentalAgreementEntitiesByOfferId(id)
            .map { it.map(RentalAgreementEntity::asExternalModel) }

    override suspend fun syncWith(
        pmPreferences: PmPreferencesDataSource,
        ioDispatcher: CoroutineDispatcher,
    ): Boolean = withContext(ioDispatcher) {
        val lastSync = pmPreferences.getLastRentalSyncTime()

        val localChanged = rentalAgreementDao.getRentalAgreementsUpdatedAfter(lastSync)
        if (localChanged.isNotEmpty()) {
            val networkModels = localChanged.map { it.asNetworkModel() }
            network.updateRentalAgreements(networkModels)
        }
        val updatedFromBackend = network.getRentalAgreementsUpdatedAfter(lastSync)
        if (updatedFromBackend.isNotEmpty()) {
            rentalAgreementDao.upsertRentalAgreements(updatedFromBackend.map { it.asEntity() })
        }
        val newSyncTime =
            (localChanged.map { it.updatedAt } + updatedFromBackend.map { it.updatedAt }).maxOrNull()
                ?: lastSync
        if (newSyncTime > lastSync) {
            pmPreferences.setLastRentalSyncTime(newSyncTime)
        }
        return@withContext true
    }

}