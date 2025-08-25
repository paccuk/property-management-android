package org.stkachenko.propertymanagement.core.database.dao.profile

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import org.stkachenko.propertymanagement.core.database.model.profile.ProfileEntity
import org.stkachenko.propertymanagement.core.database.model.profile.ProfileWithImages

@Dao
interface ProfileDao {

    @Query(
        value = """
            SELECT * FROM profiles
            WHERE userId = :profileId
        """,
    )
    fun getProfileEntity(profileId: String): Flow<ProfileEntity>

    @Query(
        value = """
            SELECT * FROM profiles
            WHERE userId IN (:ids)
        """,
    )
    fun getProfileEntities(ids: Set<String>): Flow<List<ProfileEntity>>

    @Query(
        value = """
            SELECT 
                p.*,
                ia.*,
                i.* 
            FROM profiles p
            LEFT JOIN image_attachments ia ON ia.relatedId = p.userId AND ia.relatedType = 'profile'
            LEFT JOIN images            i  ON i.id         = ia.imageId
            WHERE p.userId = :userId
            ORDER BY p.firstName ASC, p.lastName ASC
        """
    )
    fun getProfileEntitiesByUserId(userId: String): Flow<List<ProfileWithImages>>

    @Query(
        value = """
            SELECT 
                p.*,
                ia.*,
                i.* 
            FROM profiles p
            LEFT JOIN image_attachments ia ON ia.relatedId = p.userId AND ia.relatedType = 'profile'
            LEFT JOIN images            i  ON i.id         = ia.imageId
            WHERE p.userId IN (:ids)
            ORDER BY p.firstName ASC, p.lastName ASC
        """
    )
    fun getProfileEntitiesByUserIds(ids: Set<String>): Flow<List<ProfileWithImages>>


    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertOrIgnoreProfiles(profileEntities: List<ProfileEntity>): List<Long>

    @Upsert
    suspend fun upsertProfiles(entities: List<ProfileEntity>)

    @Query(
        value = """
            DELETE FROM profiles
            WHERE userId IN (:ids)
        """,
    )
    suspend fun deleteProfiles(ids: List<String>)
}