package org.stkachenko.propertymanagement.core.database.dao.chat

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import org.stkachenko.propertymanagement.core.database.model.chat.ChatEntity
import org.stkachenko.propertymanagement.core.database.model.chat.ChatWithImages

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

    @Query(
        value = """
            SELECT 
                c.*,
                
                ia.imageId ia_imageId,
                ia.relatedType ia_relatedType,
                ia.relatedId ia_relatedId,
                ia.imageType ia_imageType,
                ia.position ia_position,
                ia.createdAt ia_createdAt,
                ia.updatedAt ia_updatedAt,
                
                i.id i_id, 
                i.url i_url
            FROM chats c
            INNER JOIN chat_participants cp ON cp.chatId    = c.id
            LEFT  JOIN image_attachments ia ON ia.relatedId = c.id AND ia.relatedType = 'chat'
            LEFT  JOIN images            i  ON i.id         = ia.imageId
            WHERE cp.userId = :userId
            ORDER BY c.lastMessageId DESC
        """,
    )
    fun getChatEntitiesByUserId(userId: String): Flow<List<ChatWithImages>>

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