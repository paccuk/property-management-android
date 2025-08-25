package org.stkachenko.propertymanagement.core.database.dao.rental

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import org.stkachenko.propertymanagement.core.database.model.rental.RentalInviteEntity

@Dao
interface RentalInviteDao {

    @Query(
        value = """
            SELECT * FROM rental_invites
            WHERE id = :inviteId
        """,
    )
    fun getRentalInviteEntity(inviteId: String): Flow<RentalInviteEntity>

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertOrIgnoreRentalInvites(entities: List<RentalInviteEntity>): List<Long>

    @Upsert
    suspend fun upsertRentalInvites(entities: List<RentalInviteEntity>)

    @Query(
        value = """
            DELETE FROM rental_invites
            WHERE id IN (:ids)
        """,
    )
    suspend fun deleteRentalInvites(ids: List<String>)
}