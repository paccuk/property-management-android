package org.stkachenko.propertymanagement.core.database.dao.property

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import org.stkachenko.propertymanagement.core.database.model.property.PropertyEntity
import org.stkachenko.propertymanagement.core.database.model.property.PropertyWithImages

@Dao
interface PropertyDao {

    @Query(
        value = """
            SELECT * FROM properties
            WHERE id = :propertyId
        """,
    )
    fun getPropertyEntity(propertyId: String): Flow<PropertyEntity>

    @Query(
        value = """
            SELECT * FROM properties
            WHERE id IN (:ids)
        """,
    )
    fun getPropertyEntities(ids: List<String>): Flow<List<PropertyEntity>>

    @Query(
        value = """
            SELECT
                p.*,
                
                ia.imageId ia_imageId,
                ia.relatedType ia_relatedType,
                ia.relatedId ia_relatedId,
                ia.imageType ia_imageType,
                ia.position ia_position,
                ia.createdAt ia_createdAt,
                ia.updatedAt ia_updatedAt,
                
                i.id i_id,
                i.url i_url
            FROM properties p
            LEFT JOIN image_attachments ia ON ia.relatedId = p.id AND ia.relatedType = 'property'
            LEFT JOIN images            i  ON i.id         = ia.imageId
            WHERE p.ownerId = :id
            ORDER BY createdAt DESC
        """,
    )
    fun getPropertyEntitiesByOwnerId(id: String): Flow<List<PropertyWithImages>>

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertOrIgnoreProperties(entities: List<PropertyEntity>): List<Long>


    @Upsert
    suspend fun upsertProperties(entities: List<PropertyEntity>)

    @Transaction
    @Query(
        value = """
            DELETE FROM properties
            WHERE id IN (:ids)
        """,
    )
    suspend fun deleteProperties(ids: List<String>)
}