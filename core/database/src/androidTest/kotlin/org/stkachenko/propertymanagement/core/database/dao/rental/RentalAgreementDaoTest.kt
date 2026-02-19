package org.stkachenko.propertymanagement.core.database.dao.rental

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.stkachenko.propertymanagement.core.database.DatabaseTest
import org.stkachenko.propertymanagement.core.database.model.property.PropertyEntity
import org.stkachenko.propertymanagement.core.database.model.rental.RentalAgreementEntity
import org.stkachenko.propertymanagement.core.database.model.rental.RentalOfferEntity
import org.stkachenko.propertymanagement.core.database.model.user.UserEntity
import org.stkachenko.propertymanagement.core.model.data.user.UserRole
import java.util.Date
import kotlin.test.assertEquals

internal class RentalAgreementDaoTest : DatabaseTest() {

    @Test
    fun getRentalAgreementEntitiesByOfferId_returnsAgreementsForOffer() = runTest {
        setupDependencies()

        val agreementEntities = listOf(
            testRentalAgreementEntity(id = "1", offerId = "offer1", tenantId = "user1"),
            testRentalAgreementEntity(id = "2", offerId = "offer1", tenantId = "user1"),
            testRentalAgreementEntity(id = "3", offerId = "offer1", tenantId = "user1"),
        )
        rentalAgreementDao.upsertRentalAgreements(agreementEntities)

        val savedAgreements =
            rentalAgreementDao.getRentalAgreementEntitiesByOfferId("offer1").first()

        assertEquals(
            listOf("1", "2", "3"),
            savedAgreements.map { it.id },
        )
    }

    @Test
    fun getRentalAgreementsUpdatedAfter_returnsAgreementsWithNewerTimestamp() = runTest {
        setupDependencies()

        val agreementEntities = listOf(
            testRentalAgreementEntity(
                id = "1",
                offerId = "offer1",
                tenantId = "user1",
                updatedAt = 100L
            ),
            testRentalAgreementEntity(
                id = "2",
                offerId = "offer1",
                tenantId = "user1",
                updatedAt = 200L
            ),
            testRentalAgreementEntity(
                id = "3",
                offerId = "offer1",
                tenantId = "user1",
                updatedAt = 300L
            ),
            testRentalAgreementEntity(
                id = "4",
                offerId = "offer1",
                tenantId = "user1",
                updatedAt = 400L
            ),
        )
        rentalAgreementDao.upsertRentalAgreements(agreementEntities)

        val updatedAgreements = rentalAgreementDao.getRentalAgreementsUpdatedAfter(200L)

        assertEquals(
            setOf("3", "4"),
            updatedAgreements.map { it.id }.toSet(),
        )
    }

    @Test
    fun insertOrIgnoreRentalAgreements_ignoresExistingAgreements() = runTest {
        setupDependencies()

        val initialAgreement = testRentalAgreementEntity(
            id = "1",
            offerId = "offer1",
            tenantId = "user1",
            status = "pending"
        )
        rentalAgreementDao.upsertRentalAgreements(listOf(initialAgreement))

        val newAgreements = listOf(
            testRentalAgreementEntity(
                id = "1",
                offerId = "offer1",
                tenantId = "user1",
                status = "active"
            ),
            testRentalAgreementEntity(
                id = "2",
                offerId = "offer1",
                tenantId = "user1",
                status = "pending"
            ),
        )
        rentalAgreementDao.insertOrIgnoreRentalAgreements(newAgreements)

        val savedAgreement =
            rentalAgreementDao.getRentalAgreementEntitiesByOfferId("offer1").first()
                .find { it.id == "1" }

        assertEquals("pending", savedAgreement?.status)
    }

    @Test
    fun upsertRentalAgreements_updatesExistingAgreements() = runTest {
        setupDependencies()

        val initialAgreement = testRentalAgreementEntity(
            id = "1",
            offerId = "offer1",
            tenantId = "user1",
            status = "pending"
        )
        rentalAgreementDao.upsertRentalAgreements(listOf(initialAgreement))

        val updatedAgreement = testRentalAgreementEntity(
            id = "1",
            offerId = "offer1",
            tenantId = "user1",
            status = "active"
        )
        rentalAgreementDao.upsertRentalAgreements(listOf(updatedAgreement))

        val savedAgreement =
            rentalAgreementDao.getRentalAgreementEntitiesByOfferId("offer1").first()
                .find { it.id == "1" }

        assertEquals("active", savedAgreement?.status)
    }

    @Test
    fun deleteRentalAgreements_byId() = runTest {
        setupDependencies()

        val agreementEntities = listOf(
            testRentalAgreementEntity(id = "1", offerId = "offer1", tenantId = "user1"),
            testRentalAgreementEntity(id = "2", offerId = "offer1", tenantId = "user1"),
            testRentalAgreementEntity(id = "3", offerId = "offer1", tenantId = "user1"),
            testRentalAgreementEntity(id = "4", offerId = "offer1", tenantId = "user1"),
        )
        rentalAgreementDao.upsertRentalAgreements(agreementEntities)

        val (toDelete, toKeep) = agreementEntities.partition { it.id.toInt() % 2 == 0 }

        rentalAgreementDao.deleteRentalAgreements(toDelete.map(RentalAgreementEntity::id))

        val remainingAgreements =
            rentalAgreementDao.getRentalAgreementEntitiesByOfferId("offer1").first()

        assertEquals(
            toKeep.map(RentalAgreementEntity::id).toSet(),
            remainingAgreements.map { it.id }.toSet(),
        )
    }

    private suspend fun setupDependencies() {
        val user = testUserEntity(id = "user1")
        userDao.upsertUsers(listOf(user))

        val property = testPropertyEntity(id = "property1", ownerId = "user1")
        propertyDao.upsertProperties(listOf(property))

        val rentalOffer =
            testRentalOfferEntity(id = "offer1", propertyId = "property1", ownerId = "user1")
        rentalOfferDao.upsertRentalOffers(listOf(rentalOffer))
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

private fun testRentalOfferEntity(id: String, propertyId: String, ownerId: String) =
    RentalOfferEntity(
        id = id,
        propertyId = propertyId,
        ownerId = ownerId,
        availableFrom = Date(),
        availableTo = Date(),
        minDuration = 1L,
        maxDuration = 12L,
        maxTenants = 2,
        isActive = true,
        pricePerPerson = 500.0,
        currency = "USD",
        termsText = "Test terms",
        createdAt = 0L,
        updatedAt = 0L,
    )

private fun testRentalAgreementEntity(
    id: String,
    offerId: String,
    tenantId: String,
    status: String = "active",
    updatedAt: Long = 0L,
) = RentalAgreementEntity(
    id = id,
    offerId = offerId,
    tenantId = tenantId,
    startDate = Date(),
    endDate = Date(),
    status = status,
    createdAt = 0L,
    updatedAt = updatedAt,
)

