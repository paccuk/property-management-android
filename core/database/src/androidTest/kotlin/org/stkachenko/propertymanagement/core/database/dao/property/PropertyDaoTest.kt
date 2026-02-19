package org.stkachenko.propertymanagement.core.database.dao.property

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.stkachenko.propertymanagement.core.database.DatabaseTest
import org.stkachenko.propertymanagement.core.database.model.property.PropertyEntity
import org.stkachenko.propertymanagement.core.database.model.user.UserEntity
import org.stkachenko.propertymanagement.core.model.data.user.UserRole
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class PropertyDaoTest : DatabaseTest() {

    @Test
    fun getPropertyEntitiesByOwnerId_returnsPropertiesForOwner() = runTest {
        val owner = testUserEntity(id = "owner1")
        userDao.upsertUsers(listOf(owner))

        val propertyEntities = listOf(
            testPropertyEntity(id = "1", ownerId = "owner1"),
            testPropertyEntity(id = "2", ownerId = "owner1"),
            testPropertyEntity(id = "3", ownerId = "owner1"),
        )
        propertyDao.upsertProperties(propertyEntities)

        val savedProperties = propertyDao.getPropertyEntitiesByOwnerId("owner1").first()

        assertEquals(
            listOf("1", "2", "3"),
            savedProperties.map { it.id },
        )
    }

    @Test
    fun getPropertyEntitiesByIds_returnsMatchingProperties() = runTest {
        val owner = testUserEntity(id = "owner1")
        userDao.upsertUsers(listOf(owner))

        val propertyEntities = listOf(
            testPropertyEntity(id = "1", ownerId = "owner1"),
            testPropertyEntity(id = "2", ownerId = "owner1"),
            testPropertyEntity(id = "3", ownerId = "owner1"),
            testPropertyEntity(id = "4", ownerId = "owner1"),
        )
        propertyDao.upsertProperties(propertyEntities)

        val savedProperties = propertyDao.getPropertyEntitiesByIds(listOf("1", "3")).first()

        assertEquals(
            setOf("1", "3"),
            savedProperties.map { it.id }.toSet(),
        )
    }

    @Test
    fun getPropertyEntityById_returnsProperty() = runTest {
        val owner = testUserEntity(id = "owner1")
        userDao.upsertUsers(listOf(owner))

        val propertyEntities = listOf(
            testPropertyEntity(id = "1", ownerId = "owner1", price = 100.0),
            testPropertyEntity(id = "2", ownerId = "owner1", price = 200.0),
        )
        propertyDao.upsertProperties(propertyEntities)

        val savedProperty = propertyDao.getPropertyEntityById("2").first()

        assertEquals("2", savedProperty?.id)
        assertEquals(200.0, savedProperty?.price)
    }

    @Test
    fun getPropertyEntityById_returnsNull_whenNotFound() = runTest {
        val result = propertyDao.getPropertyEntityById("nonexistent").first()

        assertNull(result)
    }

    @Test
    fun getPropertiesUpdatedAfter_returnsPropertiesWithNewerTimestamp() = runTest {
        val owner = testUserEntity(id = "owner1")
        userDao.upsertUsers(listOf(owner))

        val propertyEntities = listOf(
            testPropertyEntity(id = "1", ownerId = "owner1", updatedAt = 100L),
            testPropertyEntity(id = "2", ownerId = "owner1", updatedAt = 200L),
            testPropertyEntity(id = "3", ownerId = "owner1", updatedAt = 300L),
            testPropertyEntity(id = "4", ownerId = "owner1", updatedAt = 400L),
        )
        propertyDao.upsertProperties(propertyEntities)

        val updatedProperties = propertyDao.getPropertiesUpdatedAfter(200L)

        assertEquals(
            setOf("3", "4"),
            updatedProperties.map { it.id }.toSet(),
        )
    }

    @Test
    fun insertOrIgnoreProperties_ignoresExistingProperties() = runTest {
        val owner = testUserEntity(id = "owner1")
        userDao.upsertUsers(listOf(owner))

        val initialProperty = testPropertyEntity(id = "1", ownerId = "owner1", price = 100.0)
        propertyDao.upsertProperties(listOf(initialProperty))

        val newProperties = listOf(
            testPropertyEntity(id = "1", ownerId = "owner1", price = 999.0),
            testPropertyEntity(id = "2", ownerId = "owner1", price = 200.0),
        )
        propertyDao.insertOrIgnoreProperties(newProperties)

        val savedProperty = propertyDao.getPropertyEntityById("1").first()

        assertEquals(100.0, savedProperty?.price)
    }

    @Test
    fun upsertProperties_updatesExistingProperties() = runTest {
        val owner = testUserEntity(id = "owner1")
        userDao.upsertUsers(listOf(owner))

        val initialProperty = testPropertyEntity(id = "1", ownerId = "owner1", price = 100.0)
        propertyDao.upsertProperties(listOf(initialProperty))

        val updatedProperty = testPropertyEntity(id = "1", ownerId = "owner1", price = 999.0)
        propertyDao.upsertProperties(listOf(updatedProperty))

        val savedProperty = propertyDao.getPropertyEntityById("1").first()

        assertEquals(999.0, savedProperty?.price)
    }

    @Test
    fun deleteProperties_byId() = runTest {
        val owner = testUserEntity(id = "owner1")
        userDao.upsertUsers(listOf(owner))

        val propertyEntities = listOf(
            testPropertyEntity(id = "1", ownerId = "owner1"),
            testPropertyEntity(id = "2", ownerId = "owner1"),
            testPropertyEntity(id = "3", ownerId = "owner1"),
            testPropertyEntity(id = "4", ownerId = "owner1"),
        )
        propertyDao.upsertProperties(propertyEntities)

        val (toDelete, toKeep) = propertyEntities.partition { it.id.toInt() % 2 == 0 }

        propertyDao.deleteProperties(toDelete.map(PropertyEntity::id))

        val remainingProperties = propertyDao.getPropertyEntitiesByIds(
            propertyEntities.map { it.id }
        ).first()

        assertEquals(
            toKeep.map(PropertyEntity::id).toSet(),
            remainingProperties.map { it.id }.toSet(),
        )
    }
}

private fun testUserEntity(
    id: String,
) = UserEntity(
    id = id,
    firstName = "Test",
    lastName = "User",
    email = "test@test.com",
    phone = "+480000000000",
    role = UserRole.OWNER,
    avatarImageUrl = "",
    updatedAt = 0L,
    createdAt = 0L,
)

private fun testPropertyEntity(
    id: String,
    ownerId: String,
    price: Double = 100.0,
    updatedAt: Long = 0L,
) = PropertyEntity(
    id = id,
    ownerId = ownerId,
    price = price,
    currency = "USD",
    type = "apartment",
    area = 50.0,
    isAvailable = true,
    address = emptyMap(),
    attributes = emptyMap(),
    images = emptyList(),
    createdAt = 0L,
    updatedAt = updatedAt,
)
