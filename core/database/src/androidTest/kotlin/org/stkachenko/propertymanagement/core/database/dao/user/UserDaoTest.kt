package org.stkachenko.propertymanagement.core.database.dao.user

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.stkachenko.propertymanagement.core.database.DatabaseTest
import org.stkachenko.propertymanagement.core.database.model.user.UserEntity
import org.stkachenko.propertymanagement.core.model.data.user.UserRole
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class UserDaoTest : DatabaseTest() {

    @Test
    fun getUserEntityById_returnsUser() = runTest {
        val userEntities = listOf(
            testUserEntity(id = "user1", firstName = "John"),
            testUserEntity(id = "user2", firstName = "Jane"),
        )
        userDao.upsertUsers(userEntities)

        val savedUser = userDao.getUserEntityById("user1").first()

        assertEquals("user1", savedUser?.id)
        assertEquals("John", savedUser?.firstName)
    }

    @Test
    fun getUserEntityById_returnsNull_whenNotFound() = runTest {
        val result = userDao.getUserEntityById("nonexistent").first()

        assertNull(result)
    }

    @Test
    fun getUsersUpdatedAfter_returnsUsersWithNewerTimestamp() = runTest {
        val userEntities = listOf(
            testUserEntity(id = "user1", updatedAt = 100L),
            testUserEntity(id = "user2", updatedAt = 200L),
            testUserEntity(id = "user3", updatedAt = 300L),
            testUserEntity(id = "user4", updatedAt = 400L),
        )
        userDao.upsertUsers(userEntities)

        val updatedUsers = userDao.getUsersUpdatedAfter(200L)

        assertEquals(
            setOf("user3", "user4"),
            updatedUsers.map { it.id }.toSet(),
        )
    }

    @Test
    fun insertOrIgnoreUsers_ignoresExistingUsers() = runTest {
        val initialUser = testUserEntity(id = "user1", firstName = "Initial")
        userDao.upsertUsers(listOf(initialUser))

        val newUsers = listOf(
            testUserEntity(id = "user1", firstName = "Updated"),
            testUserEntity(id = "user2", firstName = "New"),
        )
        userDao.insertOrIgnoreUsers(newUsers)

        val savedUser = userDao.getUserEntityById("user1").first()

        assertEquals("Initial", savedUser?.firstName)
    }

    @Test
    fun upsertUsers_updatesExistingUsers() = runTest {
        val initialUser = testUserEntity(id = "user1", firstName = "Initial")
        userDao.upsertUsers(listOf(initialUser))

        val updatedUser = testUserEntity(id = "user1", firstName = "Updated")
        userDao.upsertUsers(listOf(updatedUser))

        val savedUser = userDao.getUserEntityById("user1").first()

        assertEquals("Updated", savedUser?.firstName)
    }

    @Test
    fun deleteUsers_byId() = runTest {
        val userEntities = listOf(
            testUserEntity(id = "user1"),
            testUserEntity(id = "user2"),
            testUserEntity(id = "user3"),
            testUserEntity(id = "user4"),
        )
        userDao.upsertUsers(userEntities)

        val (toDelete, toKeep) = userEntities.partition { it.id.last().digitToInt() % 2 == 0 }

        userDao.deleteUsers(toDelete.map(UserEntity::id))

        val remainingUsers = toKeep.mapNotNull { userDao.getUserEntityById(it.id).first() }

        assertEquals(
            toKeep.map(UserEntity::id).toSet(),
            remainingUsers.map { it.id }.toSet(),
        )
    }
}

private fun testUserEntity(
    id: String,
    firstName: String = "Test",
    updatedAt: Long = 0L,
) = UserEntity(
    id = id,
    firstName = firstName,
    lastName = "User",
    email = "test@test.com",
    phone = "+480000000000",
    role = UserRole.OWNER,
    avatarImageUrl = "",
    updatedAt = updatedAt,
    createdAt = 0L,
)
