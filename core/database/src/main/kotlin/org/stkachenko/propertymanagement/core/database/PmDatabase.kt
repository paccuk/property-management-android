package org.stkachenko.propertymanagement.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
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
import org.stkachenko.propertymanagement.core.database.model.chat.ChatEntity
import org.stkachenko.propertymanagement.core.database.model.chat.ChatParticipantEntity
import org.stkachenko.propertymanagement.core.database.model.chat.MessageEntity
import org.stkachenko.propertymanagement.core.database.model.payment.InvoiceEntity
import org.stkachenko.propertymanagement.core.database.model.payment.PaymentEntity
import org.stkachenko.propertymanagement.core.database.model.payment.PaymentScheduleEntity
import org.stkachenko.propertymanagement.core.database.model.property.PropertyEntity
import org.stkachenko.propertymanagement.core.database.model.rental.RentalAgreementEntity
import org.stkachenko.propertymanagement.core.database.model.rental.RentalInviteEntity
import org.stkachenko.propertymanagement.core.database.model.rental.RentalOfferEntity
import org.stkachenko.propertymanagement.core.database.model.user.UserEntity
import org.stkachenko.propertymanagement.core.database.util.DateConverter
import org.stkachenko.propertymanagement.core.database.util.ListConverter
import org.stkachenko.propertymanagement.core.database.util.ParticipantRoleConverter
import org.stkachenko.propertymanagement.core.database.util.StringMapConverter
import org.stkachenko.propertymanagement.core.database.util.UserRoleConverter

@Database(
    entities = [
        // User
        UserEntity::class,

        // Property
        PropertyEntity::class,

        // Chat
        ChatEntity::class,
        ChatParticipantEntity::class,
        MessageEntity::class,

        // Payment
        PaymentEntity::class,
        InvoiceEntity::class,
        PaymentScheduleEntity::class,

        // Rental
        RentalOfferEntity::class,
        RentalInviteEntity::class,
        RentalAgreementEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(
    DateConverter::class,
    StringMapConverter::class,
    ParticipantRoleConverter::class,
    UserRoleConverter::class,
    ListConverter::class,
)
internal abstract class PmDatabase : RoomDatabase() {
    // User
    abstract fun userDao(): UserDao

    // Property
    abstract fun propertyDao(): PropertyDao

    // Chat
    abstract fun chatDao(): ChatDao
    abstract fun chatParticipantDao(): ChatParticipantDao
    abstract fun messageDao(): MessageDao

    // Payment
    abstract fun paymentDao(): PaymentDao
    abstract fun invoiceDao(): InvoiceDao
    abstract fun paymentScheduleDao(): PaymentScheduleDao

    // Rental
    abstract fun rentalAgreementDao(): RentalAgreementDao
    abstract fun rentalInviteDao(): RentalInviteDao
    abstract fun rentalOfferDao(): RentalOfferDao
}