package org.stkachenko.propertymanagement.core.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Before
import org.stkachenko.propertymanagement.core.database.dao.chat.ChatDao
import org.stkachenko.propertymanagement.core.database.dao.chat.ChatParticipantDao
import org.stkachenko.propertymanagement.core.database.dao.chat.MessageDao
import org.stkachenko.propertymanagement.core.database.dao.payment.InvoiceDao
import org.stkachenko.propertymanagement.core.database.dao.payment.PaymentDao
import org.stkachenko.propertymanagement.core.database.dao.payment.PaymentScheduleDao
import org.stkachenko.propertymanagement.core.database.dao.property.PropertyDao
import org.stkachenko.propertymanagement.core.database.dao.rental.RentalAgreementDao
import org.stkachenko.propertymanagement.core.database.dao.rental.RentalInviteDao
import org.stkachenko.propertymanagement.core.database.dao.rental.RentalOfferDao
import org.stkachenko.propertymanagement.core.database.dao.user.UserDao


internal abstract class DatabaseTest {

    private lateinit var db: PmDatabase
    protected lateinit var userDao: UserDao
    protected lateinit var propertyDao: PropertyDao
    protected lateinit var chatDao: ChatDao
    protected lateinit var chatParticipantDao: ChatParticipantDao
    protected lateinit var messageDao: MessageDao
    protected lateinit var paymentDao: PaymentDao
    protected lateinit var invoiceDao: InvoiceDao
    protected lateinit var paymentScheduleDao: PaymentScheduleDao
    protected lateinit var rentalAgreementDao: RentalAgreementDao
    protected lateinit var rentalInviteDao: RentalInviteDao
    protected lateinit var rentalOfferDao: RentalOfferDao

    @Before
    fun setup() {
        db = run {
            val context = ApplicationProvider.getApplicationContext<Context>()
            Room.inMemoryDatabaseBuilder(
                context,
                PmDatabase::class.java,
            ).build()
        }
        userDao = db.userDao()
        propertyDao = db.propertyDao()
        chatDao = db.chatDao()
        chatParticipantDao = db.chatParticipantDao()
        messageDao = db.messageDao()
        paymentDao = db.paymentDao()
        invoiceDao = db.invoiceDao()
        paymentScheduleDao = db.paymentScheduleDao()
        rentalAgreementDao = db.rentalAgreementDao()
        rentalInviteDao = db.rentalInviteDao()
        rentalOfferDao = db.rentalOfferDao()
    }

    @After
    fun teardown() = db.close()
}