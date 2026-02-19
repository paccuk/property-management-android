package org.stkachenko.propertymanagement.core.database.dao.rental

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import org.stkachenko.propertymanagement.core.database.model.rental.RentalOfferEntity

@Dao
interface RentalOfferDao {

    @Query(
        value = """
            SELECT * FROM rental_offers
            WHERE ownerId = :ownerId
        """,
    )
    fun getRentalOfferEntitiesByOwnerId(ownerId: String): Flow<List<RentalOfferEntity>>

    @Query(
        value = """
            SELECT * FROM rental_offers
            WHERE propertyId = :propertyId
        """,
    )
    fun getRentalOfferEntitiesByPropertyId(propertyId: String): Flow<List<RentalOfferEntity>>

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertOrIgnoreRentalOffers(entities: List<RentalOfferEntity>): List<Long>

    @Upsert
    suspend fun upsertRentalOffers(entities: List<RentalOfferEntity>)

    @Query(
        value = """
            DELETE FROM rental_offers
            WHERE id IN (:ids)
        """,
    )
    suspend fun deleteRentalOffers(ids: List<String>)

    @Query("SELECT * FROM rental_offers WHERE updatedAt > :timestamp")
    suspend fun getRentalOffersUpdatedAfter(timestamp: Long): List<RentalOfferEntity>
}