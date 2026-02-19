package org.stkachenko.propertymanagement.core.database.dao.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import org.stkachenko.propertymanagement.core.database.model.user.UserEntity

@Dao
interface UserDao {

    @Query(
        value = """
            SELECT * FROM users
            WHERE id = :id
        """,
    )
    fun getUserEntityById(id: String): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE updatedAt > :timestamp")
    suspend fun getUsersUpdatedAfter(timestamp: Long): List<UserEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreUsers(entities: List<UserEntity>): List<Long>

    @Upsert
    suspend fun upsertUsers(entities: List<UserEntity>)

    @Query(
        value = """
            DELETE FROM users
            WHERE id IN (:ids)
        """,
    )
    suspend fun deleteUsers(ids: List<String>)
}