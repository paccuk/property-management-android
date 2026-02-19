package org.stkachenko.propertymanagement.core.database.dao.rental

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import org.stkachenko.propertymanagement.core.database.model.rental.RentalAgreementEntity

@Dao
interface RentalAgreementDao {

    @Query(
        value = """
            SELECT * FROM rental_agreements
            WHERE offerId = :offerId
        """,
    )
    fun getRentalAgreementEntitiesByOfferId(offerId: String): Flow<List<RentalAgreementEntity>>

    @Query("SELECT * FROM rental_agreements WHERE updatedAt > :timestamp")
    suspend fun getRentalAgreementsUpdatedAfter(timestamp: Long): List<RentalAgreementEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreRentalAgreements(entities: List<RentalAgreementEntity>): List<Long>

    @Upsert
    suspend fun upsertRentalAgreements(entities: List<RentalAgreementEntity>)

    @Query(
        value = """
            DELETE FROM rental_agreements
            WHERE id IN (:ids)
        """,
    )
    suspend fun deleteRentalAgreements(ids: List<String>)
}