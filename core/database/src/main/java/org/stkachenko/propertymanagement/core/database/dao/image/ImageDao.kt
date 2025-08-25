package org.stkachenko.propertymanagement.core.database.dao.image

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import org.stkachenko.propertymanagement.core.database.model.image.ImageAttachmentEntity
import org.stkachenko.propertymanagement.core.database.model.image.ImageEntity
import org.stkachenko.propertymanagement.core.database.model.image.ImageWithAttachment

@Dao
interface ImageDao {

    @Query(
        value = """
            SELECT * FROM images
            WHERE id IN (:ids)
        """,
    )
    fun getImageEntitiesByIds(ids: Set<String>): Flow<List<ImageEntity>>

    @Query(
        value = """
            SELECT 
                i.*, 
                ia.*
            FROM images i
            LEFT JOIN image_attachments ia ON i.id = ia.imageId
            WHERE ia.relatedId = :relatedId
        """,
    )
    fun getImageEntitiesByRelatedId(relatedId: String): Flow<List<ImageWithAttachment>>

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertOrIgnoreImages(entities: List<ImageEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertOrIgnoreImageAttachments(entities: List<ImageAttachmentEntity>): List<Long>

    @Upsert
    suspend fun upsertImages(entities: List<ImageEntity>)

    @Upsert
    suspend fun upsertImageAttachments(entities: List<ImageAttachmentEntity>)

    @Query(
        value = """
            DELETE FROM images
            WHERE id IN (:ids)
        """,
    )
    suspend fun deleteImages(ids: List<String>)

    @Query(
        value = """
            DELETE FROM image_attachments
            WHERE imageId IN (:ids)
        """,
    )
    suspend fun deleteImageAttachments(ids: List<String>)
}