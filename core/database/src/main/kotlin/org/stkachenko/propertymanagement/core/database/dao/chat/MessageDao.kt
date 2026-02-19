package org.stkachenko.propertymanagement.core.database.dao.chat

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import org.stkachenko.propertymanagement.core.database.model.chat.MessageEntity

@Dao
interface MessageDao {

    @Query(
        value = """
            SELECT * FROM messages
            WHERE chatId = :chatId
            ORDER BY createdAt ASC
        """,
    )
    fun getMessageEntitiesByChatId(chatId: String): Flow<List<MessageEntity>>

    @Query(
        value = """
            SELECT * FROM messages
            WHERE chatId = :chatId AND createdAt > :after
            ORDER BY createdAt ASC
            LIMIT :limit
        """,
    )
    fun getFilteredMessagesByChatId(chatId: String, after: Long, limit: Int): Flow<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertOrIgnoreMessages(messageEntities: List<MessageEntity>): List<Long>

    @Upsert
    suspend fun upsertMessages(entities: List<MessageEntity>)

    @Query(
        value = """
            DELETE FROM messages
            WHERE id IN (:ids)
        """,
    )
    suspend fun deleteMessages(ids: List<String>)
}