package org.stkachenko.propertymanagement.core.database.dao.payment

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.stkachenko.propertymanagement.core.database.DatabaseTest
import org.stkachenko.propertymanagement.core.database.model.payment.InvoiceEntity
import org.stkachenko.propertymanagement.core.database.model.payment.PaymentEntity
import org.stkachenko.propertymanagement.core.database.model.payment.PaymentScheduleEntity
import org.stkachenko.propertymanagement.core.database.model.property.PropertyEntity
import org.stkachenko.propertymanagement.core.database.model.rental.RentalAgreementEntity
import org.stkachenko.propertymanagement.core.database.model.rental.RentalOfferEntity
import org.stkachenko.propertymanagement.core.database.model.user.UserEntity
import org.stkachenko.propertymanagement.core.model.data.user.UserRole
import java.util.Date
import kotlin.test.assertEquals

internal class PaymentDaoTest : DatabaseTest() {

    @Test
    fun getPaymentEntity_returnsPayment() = runTest {
        setupDependencies()

        val paymentEntities = listOf(
            testPaymentEntity(id = "payment1", amount = 100.0),
            testPaymentEntity(id = "payment2", amount = 200.0),
        )
        paymentDao.upsertPayments(paymentEntities)

        val savedPayment = paymentDao.getPaymentEntity("payment1").first()

        assertEquals("payment1", savedPayment.id)
        assertEquals(100.0, savedPayment.amount)
    }

    @Test
    fun getPaymentEntities_returnsMatchingPayments() = runTest {
        setupDependencies()

        val paymentEntities = listOf(
            testPaymentEntity(id = "payment1"),
            testPaymentEntity(id = "payment2"),
            testPaymentEntity(id = "payment3"),
            testPaymentEntity(id = "payment4"),
        )
        paymentDao.upsertPayments(paymentEntities)

        val savedPayments = paymentDao.getPaymentEntities(setOf("payment1", "payment3")).first()

        assertEquals(
            setOf("payment1", "payment3"),
            savedPayments.map { it.id }.toSet(),
        )
    }

    @Test
    fun getPaymentsUpdatedAfter_returnsPaymentsWithNewerTimestamp() = runTest {
        setupDependencies()

        val paymentEntities = listOf(
            testPaymentEntity(id = "payment1", updatedAt = 100L),
            testPaymentEntity(id = "payment2", updatedAt = 200L),
            testPaymentEntity(id = "payment3", updatedAt = 300L),
            testPaymentEntity(id = "payment4", updatedAt = 400L),
        )
        paymentDao.upsertPayments(paymentEntities)

        val updatedPayments = paymentDao.getPaymentsUpdatedAfter(200L)

        assertEquals(
            setOf("payment3", "payment4"),
            updatedPayments.map { it.id }.toSet(),
        )
    }

    @Test
    fun insertOrIgnorePayments_ignoresExistingPayments() = runTest {
        setupDependencies()

        val initialPayment = testPaymentEntity(id = "payment1", amount = 100.0)
        paymentDao.upsertPayments(listOf(initialPayment))

        val newPayments = listOf(
            testPaymentEntity(id = "payment1", amount = 999.0),
            testPaymentEntity(id = "payment2", amount = 200.0),
        )
        paymentDao.insertOrIgnorePayments(newPayments)

        val savedPayment = paymentDao.getPaymentEntity("payment1").first()

        assertEquals(100.0, savedPayment.amount)
    }

    @Test
    fun upsertPayments_updatesExistingPayments() = runTest {
        setupDependencies()

        val initialPayment = testPaymentEntity(id = "payment1", amount = 100.0)
        paymentDao.upsertPayments(listOf(initialPayment))

        val updatedPayment = testPaymentEntity(id = "payment1", amount = 999.0)
        paymentDao.upsertPayments(listOf(updatedPayment))

        val savedPayment = paymentDao.getPaymentEntity("payment1").first()

        assertEquals(999.0, savedPayment.amount)
    }

    @Test
    fun deletePayments_byId() = runTest {
        setupDependencies()

        val paymentEntities = listOf(
            testPaymentEntity(id = "payment1"),
            testPaymentEntity(id = "payment2"),
            testPaymentEntity(id = "payment3"),
            testPaymentEntity(id = "payment4"),
        )
        paymentDao.upsertPayments(paymentEntities)

        val (toDelete, toKeep) = paymentEntities.partition { it.id.last().digitToInt() % 2 == 0 }

        paymentDao.deletePayments(toDelete.map(PaymentEntity::id))

        val remainingPayments = paymentDao.getPaymentEntities(
            paymentEntities.map { it.id }.toSet()
        ).first()

        assertEquals(
            toKeep.map(PaymentEntity::id).toSet(),
            remainingPayments.map { it.id }.toSet(),
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

        val invoice = testInvoiceEntity(id = "invoice1", scheduleId = "schedule1")
        invoiceDao.upsertInvoices(listOf(invoice))
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

private fun testInvoiceEntity(id: String, scheduleId: String) = InvoiceEntity(
    id = id,
    scheduleId = scheduleId,
    invoiceNumber = "INV-001",
    issuedAt = System.currentTimeMillis(),
    status = "pending",
    createdAt = 0L,
    updatedAt = 0L,
)

private fun testPaymentEntity(
    id: String,
    amount: Double = 500.0,
    updatedAt: Long = 0L,
) = PaymentEntity(
    id = id,
    invoiceId = "invoice1",
    payerId = "user1",
    payeeId = "user1",
    amount = amount,
    currency = "USD",
    paymentMethod = "bank_transfer",
    paidAt = System.currentTimeMillis(),
    status = "completed",
    transactionReference = "TRX-001",
    createdAt = 0L,
    updatedAt = updatedAt,
)

