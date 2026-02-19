package org.stkachenko.propertymanagement.core.database.dao.property

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import org.stkachenko.propertymanagement.core.database.model.property.PropertyEntity

@Dao
interface PropertyDao {
    @Query(value = "SELECT * FROM properties WHERE id = :ownerId ")
    fun getPropertyEntitiesByOwnerId(ownerId: String): Flow<List<PropertyEntity>>

    @Query(value = "SELECT * FROM properties WHERE id IN (:ids)")
    fun getPropertyEntitiesByIds(ids: List<String>): Flow<List<PropertyEntity>>

    @Query(value = "SELECT * FROM properties WHERE id = :id")
    fun getPropertyEntityById(id: String): Flow<PropertyEntity?>

    @Query(value = "SELECT * FROM properties WHERE updatedAt > :timestamp")
    suspend fun getPropertiesUpdatedAfter(timestamp: Long): List<PropertyEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreProperties(entities: List<PropertyEntity>): List<Long>

    @Upsert
    suspend fun upsertProperties(entities: List<PropertyEntity>)

    @Query(value = "DELETE FROM properties WHERE id IN (:ids)")
    suspend fun deleteProperties(ids: List<String>)
}