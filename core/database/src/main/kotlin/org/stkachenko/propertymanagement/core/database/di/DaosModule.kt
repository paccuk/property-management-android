package org.stkachenko.propertymanagement.core.database.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.stkachenko.propertymanagement.core.database.PmDatabase
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

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {

    // User
    @Provides
    fun providesUserDao(
        database: PmDatabase,
    ): UserDao = database.userDao()

    // Property
    @Provides
    fun providesPropertyDao(
        database: PmDatabase,
    ): PropertyDao = database.propertyDao()

    // Chat
    @Provides
    fun providesChatDao(
        database: PmDatabase,
    ): ChatDao = database.chatDao()

    @Provides
    fun providesChatParticipantDao(
        database: PmDatabase,
    ): ChatParticipantDao = database.chatParticipantDao()

    @Provides
    fun providesMessageDao(
        database: PmDatabase,
    ): MessageDao = database.messageDao()

    // Payment
    @Provides
    fun providesPaymentDao(
        database: PmDatabase,
    ): PaymentDao = database.paymentDao()

    @Provides
    fun providesInvoiceDao(
        database: PmDatabase,
    ): InvoiceDao = database.invoiceDao()

    @Provides
    fun providesPaymentScheduleDao(
        database: PmDatabase,
    ): PaymentScheduleDao = database.paymentScheduleDao()

    // Rental
    @Provides
    fun providesRentalOfferDao(
        database: PmDatabase,
    ): RentalOfferDao = database.rentalOfferDao()

    @Provides
    fun providesRentalInviteDao(
        database: PmDatabase,
    ): RentalInviteDao = database.rentalInviteDao()

    @Provides
    fun providesRentalAgreementDao(
        database: PmDatabase,
    ): RentalAgreementDao = database.rentalAgreementDao()
}