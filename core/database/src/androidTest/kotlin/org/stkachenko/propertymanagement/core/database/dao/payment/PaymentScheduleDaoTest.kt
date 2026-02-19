package org.stkachenko.propertymanagement.core.database.dao.payment

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.stkachenko.propertymanagement.core.database.DatabaseTest
import org.stkachenko.propertymanagement.core.database.model.payment.PaymentScheduleEntity
import org.stkachenko.propertymanagement.core.database.model.property.PropertyEntity
import org.stkachenko.propertymanagement.core.database.model.rental.RentalAgreementEntity
import org.stkachenko.propertymanagement.core.database.model.rental.RentalOfferEntity
import org.stkachenko.propertymanagement.core.database.model.user.UserEntity
import org.stkachenko.propertymanagement.core.model.data.user.UserRole
import java.util.Date
import kotlin.test.assertEquals

internal class PaymentScheduleDaoTest : DatabaseTest() {
    @Test
    fun getPaymentScheduleEntitiesByAgreementId_returnsSchedulesForAgreement() = runTest {
        setupDependencies()
        val scheduleEntities = listOf(
            testPaymentScheduleEntity(id = "schedule1", agreementId = "agreement1", dueDate = 100L),
            testPaymentScheduleEntity(id = "schedule2", agreementId = "agreement1", dueDate = 200L),
            testPaymentScheduleEntity(id = "schedule3", agreementId = "agreement1", dueDate = 300L),
        )
        paymentScheduleDao.upsertPaymentSchedules(scheduleEntities)
        val savedSchedules =
            paymentScheduleDao.getPaymentScheduleEntitiesByAgreementId("agreement1").first()
        assertEquals(
            listOf("schedule1", "schedule2", "schedule3"),
            savedSchedules.map { it.id },
        )
    }

    @Test
    fun getPaymentScheduleEntitiesByAgreementId_returnsOrderedByDueDateAsc() = runTest {
        setupDependencies()
        val scheduleEntities = listOf(
            testPaymentScheduleEntity(id = "schedule1", agreementId = "agreement1", dueDate = 300L),
            testPaymentScheduleEntity(id = "schedule2", agreementId = "agreement1", dueDate = 100L),
            testPaymentScheduleEntity(id = "schedule3", agreementId = "agreement1", dueDate = 200L),
        )
        paymentScheduleDao.upsertPaymentSchedules(scheduleEntities)
        val savedSchedules =
            paymentScheduleDao.getPaymentScheduleEntitiesByAgreementId("agreement1").first()
        assertEquals(
            listOf(100L, 200L, 300L),
            savedSchedules.map { it.dueDate },
        )
    }

    @Test
    fun getPaymentScheduleEntities_returnsMatchingSchedules() = runTest {
        setupDependencies()
        val scheduleEntities = listOf(
            testPaymentScheduleEntity(id = "schedule1", agreementId = "agreement1"),
            testPaymentScheduleEntity(id = "schedule2", agreementId = "agreement1"),
            testPaymentScheduleEntity(id = "schedule3", agreementId = "agreement1"),
            testPaymentScheduleEntity(id = "schedule4", agreementId = "agreement1"),
        )
        paymentScheduleDao.upsertPaymentSchedules(scheduleEntities)
        val savedSchedules =
            paymentScheduleDao.getPaymentScheduleEntities(setOf("schedule1", "schedule3")).first()
        assertEquals(
            setOf("schedule1", "schedule3"),
            savedSchedules.map { it.id }.toSet(),
        )
    }

    @Test
    fun getPaymentSchedulesUpdatedAfter_returnsSchedulesWithNewerTimestamp() = runTest {
        setupDependencies()
        val scheduleEntities = listOf(
            testPaymentScheduleEntity(
                id = "schedule1",
                agreementId = "agreement1",
                updatedAt = 100L
            ),
            testPaymentScheduleEntity(
                id = "schedule2",
                agreementId = "agreement1",
                updatedAt = 200L
            ),
            testPaymentScheduleEntity(
                id = "schedule3",
                agreementId = "agreement1",
                updatedAt = 300L
            ),
            testPaymentScheduleEntity(
                id = "schedule4",
                agreementId = "agreement1",
                updatedAt = 400L
            ),
        )
        paymentScheduleDao.upsertPaymentSchedules(scheduleEntities)
        val updatedSchedules = paymentScheduleDao.getPaymentSchedulesUpdatedAfter(200L)
        assertEquals(
            setOf("schedule3", "schedule4"),
            updatedSchedules.map { it.id }.toSet(),
        )
    }

    @Test
    fun insertOrIgnorePaymentSchedules_ignoresExistingSchedules() = runTest {
        setupDependencies()
        val initialSchedule =
            testPaymentScheduleEntity(id = "schedule1", agreementId = "agreement1", amount = 100.0)
        paymentScheduleDao.upsertPaymentSchedules(listOf(initialSchedule))
        val newSchedules = listOf(
            testPaymentScheduleEntity(id = "schedule1", agreementId = "agreement1", amount = 999.0),
            testPaymentScheduleEntity(id = "schedule2", agreementId = "agreement1", amount = 200.0),
        )
        paymentScheduleDao.insertOrIgnorePaymentSchedules(newSchedules)
        val savedSchedule =
            paymentScheduleDao.getPaymentScheduleEntities(setOf("schedule1")).first().first()
        assertEquals(100.0, savedSchedule.amount)
    }

    @Test
    fun upsertPaymentSchedules_updatesExistingSchedules() = runTest {
        setupDependencies()
        val initialSchedule =
            testPaymentScheduleEntity(id = "schedule1", agreementId = "agreement1", amount = 100.0)
        paymentScheduleDao.upsertPaymentSchedules(listOf(initialSchedule))
        val updatedSchedule =
            testPaymentScheduleEntity(id = "schedule1", agreementId = "agreement1", amount = 999.0)
        paymentScheduleDao.upsertPaymentSchedules(listOf(updatedSchedule))
        val savedSchedule =
            paymentScheduleDao.getPaymentScheduleEntities(setOf("schedule1")).first().first()
        assertEquals(999.0, savedSchedule.amount)
    }

    @Test
    fun deletePaymentSchedules_byId() = runTest {
        setupDependencies()
        val scheduleEntities = listOf(
            testPaymentScheduleEntity(id = "schedule1", agreementId = "agreement1"),
            testPaymentScheduleEntity(id = "schedule2", agreementId = "agreement1"),
            testPaymentScheduleEntity(id = "schedule3", agreementId = "agreement1"),
            testPaymentScheduleEntity(id = "schedule4", agreementId = "agreement1"),
        )
        paymentScheduleDao.upsertPaymentSchedules(scheduleEntities)
        val (toDelete, toKeep) = scheduleEntities.partition { it.id.last().digitToInt() % 2 == 0 }
        paymentScheduleDao.deletePaymentSchedules(toDelete.map(PaymentScheduleEntity::id))
        val remainingSchedules = paymentScheduleDao.getPaymentScheduleEntities(
            scheduleEntities.map { it.id }.toSet()
        ).first()
        assertEquals(
            toKeep.map(PaymentScheduleEntity::id).toSet(),
            remainingSchedules.map { it.id }.toSet(),
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
        val rentalAgreement =
            testRentalAgreementEntity(id = "agreement1", offerId = "offer1", tenantId = "user1")
        rentalAgreementDao.upsertRentalAgreements(listOf(rentalAgreement))
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

private fun testRentalAgreementEntity(id: String, offerId: String, tenantId: String) =
    RentalAgreementEntity(
        id = id,
        offerId = offerId,
        tenantId = tenantId,
        startDate = Date(),
        endDate = Date(),
        status = "active",
        createdAt = 0L,
        updatedAt = 0L,
    )

private fun testPaymentScheduleEntity(
    id: String,
    agreementId: String,
    dueDate: Long = System.currentTimeMillis(),
    amount: Double = 500.0,
    updatedAt: Long = 0L,
) = PaymentScheduleEntity(
    id = id,
    agreementId = agreementId,
    dueDate = dueDate,
    amount = amount,
    currency = "USD",
    description = "Monthly rent",
    createdAt = 0L,
    updatedAt = updatedAt,
)
