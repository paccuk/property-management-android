package org.stkachenko.propertymanagement.core.database.dao.rental

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.stkachenko.propertymanagement.core.database.DatabaseTest
import org.stkachenko.propertymanagement.core.database.model.property.PropertyEntity
import org.stkachenko.propertymanagement.core.database.model.rental.RentalOfferEntity
import org.stkachenko.propertymanagement.core.database.model.user.UserEntity
import org.stkachenko.propertymanagement.core.model.data.user.UserRole
import java.util.Date
import kotlin.test.assertEquals

internal class RentalOfferDaoTest : DatabaseTest() {

    @Test
    fun getRentalOfferEntitiesByOwnerId_returnsOffersForOwner() = runTest {
        val owner = testUserEntity(id = "owner1")
        userDao.upsertUsers(listOf(owner))

        val property = testPropertyEntity(id = "property1", ownerId = "owner1")
        propertyDao.upsertProperties(listOf(property))

        val rentalOfferEntities = listOf(
            testRentalOfferEntity(id = "1", propertyId = "property1", ownerId = "owner1"),
            testRentalOfferEntity(id = "2", propertyId = "property1", ownerId = "owner1"),
            testRentalOfferEntity(id = "3", propertyId = "property1", ownerId = "owner1"),
        )
        rentalOfferDao.upsertRentalOffers(rentalOfferEntities)

        val savedOffers = rentalOfferDao.getRentalOfferEntitiesByOwnerId("owner1").first()

        assertEquals(
            listOf("1", "2", "3"),
            savedOffers.map { it.id },
        )
    }

    @Test
    fun getRentalOfferEntitiesByPropertyId_returnsOffersForProperty() = runTest {
        val owner = testUserEntity(id = "owner1")
        userDao.upsertUsers(listOf(owner))

        val properties = listOf(
            testPropertyEntity(id = "property1", ownerId = "owner1"),
            testPropertyEntity(id = "property2", ownerId = "owner1"),
        )
        propertyDao.upsertProperties(properties)

        val rentalOfferEntities = listOf(
            testRentalOfferEntity(id = "1", propertyId = "property1", ownerId = "owner1"),
            testRentalOfferEntity(id = "2", propertyId = "property1", ownerId = "owner1"),
            testRentalOfferEntity(id = "3", propertyId = "property2", ownerId = "owner1"),
        )
        rentalOfferDao.upsertRentalOffers(rentalOfferEntities)

        val savedOffers = rentalOfferDao.getRentalOfferEntitiesByPropertyId("property1").first()

        assertEquals(
            setOf("1", "2"),
            savedOffers.map { it.id }.toSet(),
        )
    }

    @Test
    fun getRentalOffersUpdatedAfter_returnsOffersWithNewerTimestamp() = runTest {
        val owner = testUserEntity(id = "owner1")
        userDao.upsertUsers(listOf(owner))

        val property = testPropertyEntity(id = "property1", ownerId = "owner1")
        propertyDao.upsertProperties(listOf(property))

        val rentalOfferEntities = listOf(
            testRentalOfferEntity(
                id = "1",
                propertyId = "property1",
                ownerId = "owner1",
                updatedAt = 100L
            ),
            testRentalOfferEntity(
                id = "2",
                propertyId = "property1",
                ownerId = "owner1",
                updatedAt = 200L
            ),
            testRentalOfferEntity(
                id = "3",
                propertyId = "property1",
                ownerId = "owner1",
                updatedAt = 300L
            ),
            testRentalOfferEntity(
                id = "4",
                propertyId = "property1",
                ownerId = "owner1",
                updatedAt = 400L
            ),
        )
        rentalOfferDao.upsertRentalOffers(rentalOfferEntities)

        val updatedOffers = rentalOfferDao.getRentalOffersUpdatedAfter(200L)

        assertEquals(
            setOf("3", "4"),
            updatedOffers.map { it.id }.toSet(),
        )
    }

    @Test
    fun insertOrIgnoreRentalOffers_ignoresExistingOffers() = runTest {
        val owner = testUserEntity(id = "owner1")
        userDao.upsertUsers(listOf(owner))

        val property = testPropertyEntity(id = "property1", ownerId = "owner1")
        propertyDao.upsertProperties(listOf(property))

        val initialOffer = testRentalOfferEntity(
            id = "1",
            propertyId = "property1",
            ownerId = "owner1",
            pricePerPerson = 100.0
        )
        rentalOfferDao.upsertRentalOffers(listOf(initialOffer))

        val newOffers = listOf(
            testRentalOfferEntity(
                id = "1",
                propertyId = "property1",
                ownerId = "owner1",
                pricePerPerson = 999.0
            ),
            testRentalOfferEntity(
                id = "2",
                propertyId = "property1",
                ownerId = "owner1",
                pricePerPerson = 200.0
            ),
        )
        rentalOfferDao.insertOrIgnoreRentalOffers(newOffers)

        val savedOffer =
            rentalOfferDao.getRentalOfferEntitiesByOwnerId("owner1").first().find { it.id == "1" }

        assertEquals(100.0, savedOffer?.pricePerPerson)
    }

    @Test
    fun upsertRentalOffers_updatesExistingOffers() = runTest {
        val owner = testUserEntity(id = "owner1")
        userDao.upsertUsers(listOf(owner))

        val property = testPropertyEntity(id = "property1", ownerId = "owner1")
        propertyDao.upsertProperties(listOf(property))

        val initialOffer = testRentalOfferEntity(
            id = "1",
            propertyId = "property1",
            ownerId = "owner1",
            pricePerPerson = 100.0
        )
        rentalOfferDao.upsertRentalOffers(listOf(initialOffer))

        val updatedOffer = testRentalOfferEntity(
            id = "1",
            propertyId = "property1",
            ownerId = "owner1",
            pricePerPerson = 999.0
        )
        rentalOfferDao.upsertRentalOffers(listOf(updatedOffer))

        val savedOffer =
            rentalOfferDao.getRentalOfferEntitiesByOwnerId("owner1").first().find { it.id == "1" }

        assertEquals(999.0, savedOffer?.pricePerPerson)
    }

    @Test
    fun deleteRentalOffers_byId() = runTest {
        val owner = testUserEntity(id = "owner1")
        userDao.upsertUsers(listOf(owner))

        val property = testPropertyEntity(id = "property1", ownerId = "owner1")
        propertyDao.upsertProperties(listOf(property))

        val rentalOfferEntities = listOf(
            testRentalOfferEntity(id = "1", propertyId = "property1", ownerId = "owner1"),
            testRentalOfferEntity(id = "2", propertyId = "property1", ownerId = "owner1"),
            testRentalOfferEntity(id = "3", propertyId = "property1", ownerId = "owner1"),
            testRentalOfferEntity(id = "4", propertyId = "property1", ownerId = "owner1"),
        )
        rentalOfferDao.upsertRentalOffers(rentalOfferEntities)

        val (toDelete, toKeep) = rentalOfferEntities.partition { it.id.toInt() % 2 == 0 }

        rentalOfferDao.deleteRentalOffers(toDelete.map(RentalOfferEntity::id))

        val remainingOffers = rentalOfferDao.getRentalOfferEntitiesByOwnerId("owner1").first()

        assertEquals(
            toKeep.map(RentalOfferEntity::id).toSet(),
            remainingOffers.map { it.id }.toSet(),
        )
    }
}

private fun testUserEntity(id: String) = UserEntity(
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

private fun testPropertyEntity(id: String, ownerId: String) = PropertyEntity(
    id = id,
    ownerId = ownerId,
    price = 100.0,
    currency = "USD",
    type = "apartment",
    area = 50.0,
    isAvailable = true,
    address = emptyMap(),
    attributes = emptyMap(),
    images = emptyList(),
    createdAt = 0L,
    updatedAt = 0L,
)

private fun testRentalOfferEntity(
    id: String,
    propertyId: String,
    ownerId: String,
    pricePerPerson: Double = 500.0,
    updatedAt: Long = 0L,
) = RentalOfferEntity(
    id = id,
    propertyId = propertyId,
    ownerId = ownerId,
    availableFrom = Date(),
    availableTo = Date(),
    minDuration = 1L,
    maxDuration = 12L,
    maxTenants = 2,
    isActive = true,
    pricePerPerson = pricePerPerson,
    currency = "USD",
    termsText = "Test terms",
    createdAt = 0L,
    updatedAt = updatedAt,
)

