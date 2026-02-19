package org.stkachenko.propertymanagement.core.database.dao.payment

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.stkachenko.propertymanagement.core.database.DatabaseTest
import org.stkachenko.propertymanagement.core.database.model.payment.InvoiceEntity
import org.stkachenko.propertymanagement.core.database.model.payment.PaymentScheduleEntity
import org.stkachenko.propertymanagement.core.database.model.rental.RentalAgreementEntity
import org.stkachenko.propertymanagement.core.database.model.rental.RentalOfferEntity
import org.stkachenko.propertymanagement.core.database.model.property.PropertyEntity
import org.stkachenko.propertymanagement.core.database.model.user.UserEntity
import org.stkachenko.propertymanagement.core.model.data.user.UserRole
import java.util.Date
import kotlin.test.assertEquals

internal class InvoiceDaoTest : DatabaseTest() {

    @Test
    fun getInvoiceEntity_returnsInvoice() = runTest {
        setupDependencies()

        val invoiceEntities = listOf(
            testInvoiceEntity(id = "invoice1", invoiceNumber = "INV-001"),
            testInvoiceEntity(id = "invoice2", invoiceNumber = "INV-002"),
        )
        invoiceDao.upsertInvoices(invoiceEntities)

        val savedInvoice = invoiceDao.getInvoiceEntity("invoice1").first()

        assertEquals("invoice1", savedInvoice.id)
        assertEquals("INV-001", savedInvoice.invoiceNumber)
    }

    @Test
    fun getInvoiceEntities_returnsMatchingInvoices() = runTest {
        setupDependencies()

        val invoiceEntities = listOf(
            testInvoiceEntity(id = "invoice1"),
            testInvoiceEntity(id = "invoice2"),
            testInvoiceEntity(id = "invoice3"),
            testInvoiceEntity(id = "invoice4"),
        )
        invoiceDao.upsertInvoices(invoiceEntities)

        val savedInvoices = invoiceDao.getInvoiceEntities(setOf("invoice1", "invoice3")).first()

        assertEquals(
            setOf("invoice1", "invoice3"),
            savedInvoices.map { it.id }.toSet(),
        )
    }

    @Test
    fun getInvoicesUpdatedAfter_returnsInvoicesWithNewerTimestamp() = runTest {
        setupDependencies()

        val invoiceEntities = listOf(
            testInvoiceEntity(id = "invoice1", updatedAt = 100L),
            testInvoiceEntity(id = "invoice2", updatedAt = 200L),
            testInvoiceEntity(id = "invoice3", updatedAt = 300L),
            testInvoiceEntity(id = "invoice4", updatedAt = 400L),
        )
        invoiceDao.upsertInvoices(invoiceEntities)

        val updatedInvoices = invoiceDao.getInvoicesUpdatedAfter(200L)

        assertEquals(
            setOf("invoice3", "invoice4"),
            updatedInvoices.map { it.id }.toSet(),
        )
    }

    @Test
    fun insertOrIgnoreInvoices_ignoresExistingInvoices() = runTest {
        setupDependencies()

        val initialInvoice = testInvoiceEntity(id = "invoice1", invoiceNumber = "INV-001")
        invoiceDao.upsertInvoices(listOf(initialInvoice))

        val newInvoices = listOf(
            testInvoiceEntity(id = "invoice1", invoiceNumber = "INV-999"),
            testInvoiceEntity(id = "invoice2", invoiceNumber = "INV-002"),
        )
        invoiceDao.insertOrIgnoreInvoices(newInvoices)

        val savedInvoice = invoiceDao.getInvoiceEntity("invoice1").first()

        assertEquals("INV-001", savedInvoice.invoiceNumber)
    }

    @Test
    fun upsertInvoices_updatesExistingInvoices() = runTest {
        setupDependencies()

        val initialInvoice = testInvoiceEntity(id = "invoice1", invoiceNumber = "INV-001")
        invoiceDao.upsertInvoices(listOf(initialInvoice))

        val updatedInvoice = testInvoiceEntity(id = "invoice1", invoiceNumber = "INV-999")
        invoiceDao.upsertInvoices(listOf(updatedInvoice))

        val savedInvoice = invoiceDao.getInvoiceEntity("invoice1").first()

        assertEquals("INV-999", savedInvoice.invoiceNumber)
    }

    @Test
    fun deleteInvoices_byId() = runTest {
        setupDependencies()

        val invoiceEntities = listOf(
            testInvoiceEntity(id = "invoice1"),
            testInvoiceEntity(id = "invoice2"),
            testInvoiceEntity(id = "invoice3"),
            testInvoiceEntity(id = "invoice4"),
        )
        invoiceDao.upsertInvoices(invoiceEntities)

        val (toDelete, toKeep) = invoiceEntities.partition { it.id.last().digitToInt() % 2 == 0 }

        invoiceDao.deleteInvoices(toDelete.map(InvoiceEntity::id))

        val remainingInvoices = invoiceDao.getInvoiceEntities(
            invoiceEntities.map { it.id }.toSet()
        ).first()

        assertEquals(
            toKeep.map(InvoiceEntity::id).toSet(),
            remainingInvoices.map { it.id }.toSet(),
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

        val paymentSchedule =
            testPaymentScheduleEntity(id = "schedule1", agreementId = "agreement1")
        paymentScheduleDao.upsertPaymentSchedules(listOf(paymentSchedule))
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

private fun testPaymentScheduleEntity(id: String, agreementId: String) = PaymentScheduleEntity(
    id = id,
    agreementId = agreementId,
    dueDate = System.currentTimeMillis(),
    amount = 500.0,
    currency = "USD",
    description = "Monthly rent",
    createdAt = 0L,
    updatedAt = 0L,
)

private fun testInvoiceEntity(
    id: String,
    invoiceNumber: String = "INV-001",
    updatedAt: Long = 0L,
) = InvoiceEntity(
    id = id,
    scheduleId = "schedule1",
    invoiceNumber = invoiceNumber,
    issuedAt = System.currentTimeMillis(),
    status = "pending",
    createdAt = 0L,
    updatedAt = updatedAt,
)

