package org.stkachenko.propertymanagement.core.database.dao.chat

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import org.stkachenko.propertymanagement.core.database.model.chat.ChatParticipantEntity

@Dao
interface ChatParticipantDao {

    @Query(
        value = """
            SELECT * FROM chat_participants
            WHERE chatId = :chatId
        """
    )
    fun getChatParticipantsByChatId(chatId: String): Flow<List<ChatParticipantEntity>>

    @Query(
        value = """
            SELECT * FROM chat_participants
            WHERE chatId IN (:chatIds)
        """
    )
    fun getChatParticipants(chatIds: List<String>): Flow<List<ChatParticipantEntity>>

    @Query(
        value = """
            SELECT * FROM chat_participants
            WHERE userId = :userId
        """
    )
    fun getChatParticipantsByUserId(userId: String): Flow<List<ChatParticipantEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreChatParticipants(chatParticipants: List<ChatParticipantEntity>): List<Long>

    @Upsert
    suspend fun upsertChatParticipants(entities: List<ChatParticipantEntity>)

    @Query(
        value = """
            DELETE FROM chat_participants
            WHERE chatId IN (:chatIds)
        """
    )
    suspend fun deleteChatParticipantsByChatIds(chatIds: List<String>)

}