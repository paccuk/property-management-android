package org.stkachenko.propertymanagement.core.database.dao.chat

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.stkachenko.propertymanagement.core.database.DatabaseTest
import org.stkachenko.propertymanagement.core.database.model.chat.ChatEntity
import org.stkachenko.propertymanagement.core.database.model.chat.ChatParticipantEntity
import org.stkachenko.propertymanagement.core.database.model.chat.MessageEntity
import org.stkachenko.propertymanagement.core.database.model.user.UserEntity
import org.stkachenko.propertymanagement.core.model.data.chat.ParticipantRole
import org.stkachenko.propertymanagement.core.model.data.user.UserRole
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class ChatDaoTest : DatabaseTest() {

    // region ChatDao tests

    @Test
    fun getChatEntity_returnsChat() = runTest {
        val chatEntities = listOf(
            testChatEntity(id = "chat1", name = "Chat 1"),
            testChatEntity(id = "chat2", name = "Chat 2"),
        )
        chatDao.upsertChats(chatEntities)

        val savedChat = chatDao.getChatEntity("chat1").first()

        assertEquals("chat1", savedChat.id)
        assertEquals("Chat 1", savedChat.name)
    }

    @Test
    fun getChatEntities_returnsMatchingChats() = runTest {
        val chatEntities = listOf(
            testChatEntity(id = "chat1"),
            testChatEntity(id = "chat2"),
            testChatEntity(id = "chat3"),
            testChatEntity(id = "chat4"),
        )
        chatDao.upsertChats(chatEntities)

        val savedChats = chatDao.getChatEntities(setOf("chat1", "chat3")).first()

        assertEquals(
            setOf("chat1", "chat3"),
            savedChats.map { it.id }.toSet(),
        )
    }

    @Test
    fun insertOrIgnoreChats_ignoresExistingChats() = runTest {
        val initialChat = testChatEntity(id = "chat1", name = "Initial Name")
        chatDao.upsertChats(listOf(initialChat))

        val newChats = listOf(
            testChatEntity(id = "chat1", name = "Updated Name"),
            testChatEntity(id = "chat2", name = "New Chat"),
        )
        chatDao.insertOrIgnoreChats(newChats)

        val savedChat = chatDao.getChatEntity("chat1").first()

        assertEquals("Initial Name", savedChat.name)
    }

    @Test
    fun upsertChats_updatesExistingChats() = runTest {
        val initialChat = testChatEntity(id = "chat1", name = "Initial Name")
        chatDao.upsertChats(listOf(initialChat))

        val updatedChat = testChatEntity(id = "chat1", name = "Updated Name")
        chatDao.upsertChats(listOf(updatedChat))

        val savedChat = chatDao.getChatEntity("chat1").first()

        assertEquals("Updated Name", savedChat.name)
    }

    @Test
    fun deleteChats_byId() = runTest {
        val chatEntities = listOf(
            testChatEntity(id = "chat1"),
            testChatEntity(id = "chat2"),
            testChatEntity(id = "chat3"),
            testChatEntity(id = "chat4"),
        )
        chatDao.upsertChats(chatEntities)

        val (toDelete, toKeep) = chatEntities.partition { it.id.last().digitToInt() % 2 == 0 }

        chatDao.deleteChats(toDelete.map(ChatEntity::id))

        val remainingChats = chatDao.getChatEntities(
            chatEntities.map { it.id }.toSet()
        ).first()

        assertEquals(
            toKeep.map(ChatEntity::id).toSet(),
            remainingChats.map { it.id }.toSet(),
        )
    }

    // endregion

    // region ChatParticipantDao tests

    @Test
    fun getChatParticipantsByChatId_returnsParticipantsForChat() = runTest {
        val user1 = testUserEntity(id = "user1")
        val user2 = testUserEntity(id = "user2")
        userDao.upsertUsers(listOf(user1, user2))

        val chat = testChatEntity(id = "chat1")
        chatDao.upsertChats(listOf(chat))

        val participants = listOf(
            testChatParticipantEntity(chatId = "chat1", userId = "user1"),
            testChatParticipantEntity(chatId = "chat1", userId = "user2"),
        )
        chatParticipantDao.upsertChatParticipants(participants)

        val savedParticipants = chatParticipantDao.getChatParticipantsByChatId("chat1").first()

        assertEquals(
            setOf("user1", "user2"),
            savedParticipants.map { it.userId }.toSet(),
        )
    }

    @Test
    fun getChatParticipantsByUserId_returnsParticipantsForUser() = runTest {
        val user = testUserEntity(id = "user1")
        userDao.upsertUsers(listOf(user))

        val chats = listOf(
            testChatEntity(id = "chat1"),
            testChatEntity(id = "chat2"),
        )
        chatDao.upsertChats(chats)

        val participants = listOf(
            testChatParticipantEntity(chatId = "chat1", userId = "user1"),
            testChatParticipantEntity(chatId = "chat2", userId = "user1"),
        )
        chatParticipantDao.upsertChatParticipants(participants)

        val savedParticipants = chatParticipantDao.getChatParticipantsByUserId("user1").first()

        assertEquals(
            setOf("chat1", "chat2"),
            savedParticipants.map { it.chatId }.toSet(),
        )
    }

    @Test
    fun getChatParticipants_returnsParticipantsForMultipleChats() = runTest {
        val users = listOf(
            testUserEntity(id = "user1"),
            testUserEntity(id = "user2"),
        )
        userDao.upsertUsers(users)

        val chats = listOf(
            testChatEntity(id = "chat1"),
            testChatEntity(id = "chat2"),
            testChatEntity(id = "chat3"),
        )
        chatDao.upsertChats(chats)

        val participants = listOf(
            testChatParticipantEntity(chatId = "chat1", userId = "user1"),
            testChatParticipantEntity(chatId = "chat2", userId = "user1"),
            testChatParticipantEntity(chatId = "chat3", userId = "user2"),
        )
        chatParticipantDao.upsertChatParticipants(participants)

        val savedParticipants = chatParticipantDao.getChatParticipants(listOf("chat1", "chat2")).first()

        assertEquals(2, savedParticipants.size)
        assertEquals(
            setOf("chat1", "chat2"),
            savedParticipants.map { it.chatId }.toSet(),
        )
    }

    @Test
    fun deleteChatParticipantsByChatIds_removesParticipants() = runTest {
        val users = listOf(
            testUserEntity(id = "user1"),
            testUserEntity(id = "user2"),
        )
        userDao.upsertUsers(users)

        val chats = listOf(
            testChatEntity(id = "chat1"),
            testChatEntity(id = "chat2"),
        )
        chatDao.upsertChats(chats)

        val participants = listOf(
            testChatParticipantEntity(chatId = "chat1", userId = "user1"),
            testChatParticipantEntity(chatId = "chat2", userId = "user2"),
        )
        chatParticipantDao.upsertChatParticipants(participants)

        chatParticipantDao.deleteChatParticipantsByChatIds(listOf("chat1"))

        val remainingParticipants = chatParticipantDao.getChatParticipants(listOf("chat1", "chat2")).first()

        assertEquals(1, remainingParticipants.size)
        assertEquals("chat2", remainingParticipants.first().chatId)
    }

    // endregion

    // region MessageDao tests

    @Test
    fun getMessageEntitiesByChatId_returnsMessagesOrderedByCreatedAtAsc() = runTest {
        val user = testUserEntity(id = "user1")
        userDao.upsertUsers(listOf(user))

        val chat = testChatEntity(id = "chat1")
        chatDao.upsertChats(listOf(chat))

        val participant = testChatParticipantEntity(chatId = "chat1", userId = "user1")
        chatParticipantDao.upsertChatParticipants(listOf(participant))

        val messages = listOf(
            testMessageEntity(id = "msg1", chatId = "chat1", authorId = "user1", createdAt = 300L),
            testMessageEntity(id = "msg2", chatId = "chat1", authorId = "user1", createdAt = 100L),
            testMessageEntity(id = "msg3", chatId = "chat1", authorId = "user1", createdAt = 200L),
        )
        messageDao.upsertMessages(messages)

        val savedMessages = messageDao.getMessageEntitiesByChatId("chat1").first()

        assertEquals(
            listOf(100L, 200L, 300L),
            savedMessages.map { it.createdAt },
        )
    }

    @Test
    fun getFilteredMessagesByChatId_returnsMessagesAfterTimestamp() = runTest {
        val user = testUserEntity(id = "user1")
        userDao.upsertUsers(listOf(user))

        val chat = testChatEntity(id = "chat1")
        chatDao.upsertChats(listOf(chat))

        val participant = testChatParticipantEntity(chatId = "chat1", userId = "user1")
        chatParticipantDao.upsertChatParticipants(listOf(participant))

        val messages = listOf(
            testMessageEntity(id = "msg1", chatId = "chat1", authorId = "user1", createdAt = 100L),
            testMessageEntity(id = "msg2", chatId = "chat1", authorId = "user1", createdAt = 200L),
            testMessageEntity(id = "msg3", chatId = "chat1", authorId = "user1", createdAt = 300L),
            testMessageEntity(id = "msg4", chatId = "chat1", authorId = "user1", createdAt = 400L),
        )
        messageDao.upsertMessages(messages)

        val filteredMessages = messageDao.getFilteredMessagesByChatId(
            chatId = "chat1",
            after = 200L,
            limit = 10
        ).first()

        assertEquals(
            listOf("msg3", "msg4"),
            filteredMessages.map { it.id },
        )
    }

    @Test
    fun getFilteredMessagesByChatId_respectsLimit() = runTest {
        val user = testUserEntity(id = "user1")
        userDao.upsertUsers(listOf(user))

        val chat = testChatEntity(id = "chat1")
        chatDao.upsertChats(listOf(chat))

        val participant = testChatParticipantEntity(chatId = "chat1", userId = "user1")
        chatParticipantDao.upsertChatParticipants(listOf(participant))

        val messages = listOf(
            testMessageEntity(id = "msg1", chatId = "chat1", authorId = "user1", createdAt = 100L),
            testMessageEntity(id = "msg2", chatId = "chat1", authorId = "user1", createdAt = 200L),
            testMessageEntity(id = "msg3", chatId = "chat1", authorId = "user1", createdAt = 300L),
            testMessageEntity(id = "msg4", chatId = "chat1", authorId = "user1", createdAt = 400L),
        )
        messageDao.upsertMessages(messages)

        val filteredMessages = messageDao.getFilteredMessagesByChatId(
            chatId = "chat1",
            after = 0L,
            limit = 2
        ).first()

        assertEquals(2, filteredMessages.size)
    }

    @Test
    fun deleteMessages_byId() = runTest {
        val user = testUserEntity(id = "user1")
        userDao.upsertUsers(listOf(user))

        val chat = testChatEntity(id = "chat1")
        chatDao.upsertChats(listOf(chat))

        val participant = testChatParticipantEntity(chatId = "chat1", userId = "user1")
        chatParticipantDao.upsertChatParticipants(listOf(participant))

        val messages = listOf(
            testMessageEntity(id = "msg1", chatId = "chat1", authorId = "user1"),
            testMessageEntity(id = "msg2", chatId = "chat1", authorId = "user1"),
            testMessageEntity(id = "msg3", chatId = "chat1", authorId = "user1"),
        )
        messageDao.upsertMessages(messages)

        messageDao.deleteMessages(listOf("msg1", "msg3"))

        val remainingMessages = messageDao.getMessageEntitiesByChatId("chat1").first()

        assertEquals(
            listOf("msg2"),
            remainingMessages.map { it.id },
        )
    }

    @Test
    fun getMessageEntitiesByChatId_returnsEmptyList_whenNoMessages() = runTest {
        val result = messageDao.getMessageEntitiesByChatId("nonexistent").first()

        assertTrue(result.isEmpty())
    }

    // endregion
}

private fun testUserEntity(
    id: String,
) = UserEntity(
    id = id,
    firstName = "Test",
    lastName = "User",
    email = "test@test.com",
    phone = "+380000000000",
    role = UserRole.OWNER,
    avatarImageUrl = "",
    updatedAt = 0L,
    createdAt = 0L,
)

private fun testChatEntity(
    id: String,
    name: String = "Test Chat",
) = ChatEntity(
    id = id,
    name = name,
    unreadCount = 0,
    isGroup = false,
    isArchived = false,
    isMuted = false,
    isPinned = false,
    isDeleted = false,
    isBlocked = false,
    lastMessageId = null,
)

private fun testChatParticipantEntity(
    chatId: String,
    userId: String,
    role: ParticipantRole = ParticipantRole.MEMBER,
) = ChatParticipantEntity(
    chatId = chatId,
    userId = userId,
    role = role,
    isActive = true,
    lastReadMessageId = null,
    joinedAt = 0L,
)

private fun testMessageEntity(
    id: String,
    chatId: String,
    authorId: String,
    createdAt: Long = 0L,
) = MessageEntity(
    id = id,
    content = "Test message",
    messageType = "text",
    status = "sent",
    isEdited = false,
    isDeleted = false,
    authorId = authorId,
    chatId = chatId,
    createdAt = createdAt,
    updatedAt = 0L,
)
