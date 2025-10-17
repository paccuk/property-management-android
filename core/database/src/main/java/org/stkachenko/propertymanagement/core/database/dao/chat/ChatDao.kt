package org.stkachenko.propertymanagement.core.database.dao.chat

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import org.stkachenko.propertymanagement.core.database.model.chat.ChatEntity

@Dao
interface ChatDao {

    @Query(
        value = """
            SELECT * FROM chats
            WHERE id = :chatId
        """,
    )
    fun getChatEntity(chatId: String): Flow<ChatEntity>

    @Query(
        value = """
            SELECT * FROM chats
            WHERE id IN (:ids)
        """,
    )
    fun getChatEntities(ids: Set<String>): Flow<List<ChatEntity>>

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertOrIgnoreChats(chatEntities: List<ChatEntity>): List<Long>

    @Upsert
    suspend fun upsertChats(entities: List<ChatEntity>)

    @Query(
        value = """
            DELETE FROM chats
            WHERE id IN (:ids)
        """,
    )
    suspend fun deleteChats(ids: List<String>)
}