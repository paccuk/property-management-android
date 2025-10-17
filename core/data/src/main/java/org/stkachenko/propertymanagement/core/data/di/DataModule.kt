package org.stkachenko.propertymanagement.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.stkachenko.propertymanagement.core.data.repository.chat.ChatParticipantsRepository
import org.stkachenko.propertymanagement.core.data.repository.chat.ChatsRepository
import org.stkachenko.propertymanagement.core.data.repository.chat.MessagesRepository
import org.stkachenko.propertymanagement.core.data.repository.chat.OfflineFirstChatParticipantsRepository
import org.stkachenko.propertymanagement.core.data.repository.chat.OfflineFirstChatsRepository
import org.stkachenko.propertymanagement.core.data.repository.chat.OfflineFirstMessagesRepository
import org.stkachenko.propertymanagement.core.data.repository.payment.InvoicesRepository
import org.stkachenko.propertymanagement.core.data.repository.payment.OfflineFirstInvoicesRepository
import org.stkachenko.propertymanagement.core.data.repository.payment.OfflineFirstPaymentSchedulesRepository
import org.stkachenko.propertymanagement.core.data.repository.payment.OfflineFirstPaymentsRepository
import org.stkachenko.propertymanagement.core.data.repository.payment.PaymentSchedulesRepository
import org.stkachenko.propertymanagement.core.data.repository.payment.PaymentsRepository
import org.stkachenko.propertymanagement.core.data.repository.property.OfflineFirstPropertiesRepository
import org.stkachenko.propertymanagement.core.data.repository.property.PropertiesRepository
import org.stkachenko.propertymanagement.core.data.repository.rental.OfflineFirstRentalAgreementsRepository
import org.stkachenko.propertymanagement.core.data.repository.rental.OfflineFirstRentalInvitesRepository
import org.stkachenko.propertymanagement.core.data.repository.rental.OfflineFirstRentalOffersRepository
import org.stkachenko.propertymanagement.core.data.repository.rental.RentalAgreementsRepository
import org.stkachenko.propertymanagement.core.data.repository.rental.RentalInvitesRepository
import org.stkachenko.propertymanagement.core.data.repository.rental.RentalOffersRepository
import org.stkachenko.propertymanagement.core.data.repository.user.OfflineFirstUsersRepository
import org.stkachenko.propertymanagement.core.data.repository.user.UserRepository
import org.stkachenko.propertymanagement.core.data.repository.userdata.OfflineFirstUserDataRepository
import org.stkachenko.propertymanagement.core.data.repository.userdata.UserDataRepository
import org.stkachenko.propertymanagement.core.data.util.ConnectivityManagerNetworkMonitor
import org.stkachenko.propertymanagement.core.data.util.NetworkMonitor
import org.stkachenko.propertymanagement.core.data.util.TimeZoneBroadcastMonitor
import org.stkachenko.propertymanagement.core.data.util.TimeZoneMonitor

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    // User
    @Binds
    internal abstract fun bindsUserRepository(
        userRepository: OfflineFirstUsersRepository,
    ): UserRepository

    @Binds
    internal abstract fun bindsUserDataRepository(
        userDataRepository: OfflineFirstUserDataRepository,
    ): UserDataRepository

    // Property
    @Binds
    internal abstract fun bindsPropertyRepository(
        propertiesRepository: OfflineFirstPropertiesRepository,
    ): PropertiesRepository

    // Chat
    @Binds
    internal abstract fun bindsChatsRepository(
        chatsRepository: OfflineFirstChatsRepository,
    ): ChatsRepository

    @Binds
    internal abstract fun bindsChatParticipantsRepository(
        chatParticipantsRepository: OfflineFirstChatParticipantsRepository,
    ): ChatParticipantsRepository

    @Binds
    internal abstract fun bindsMessagesRepository(
        messagesRepository: OfflineFirstMessagesRepository,
    ): MessagesRepository

    // Payment
    @Binds
    internal abstract fun bindsPaymentsRepository(
        paymentsRepository: OfflineFirstPaymentsRepository,
    ): PaymentsRepository

    @Binds
    internal abstract fun bindsInvoicesRepository(
        invoicesRepository: OfflineFirstInvoicesRepository,
    ): InvoicesRepository

    @Binds
    internal abstract fun bindsPaymentSchedulesRepository(
        paymentSchedulesRepository: OfflineFirstPaymentSchedulesRepository,
    ): PaymentSchedulesRepository

    // Rental
    @Binds
    internal abstract fun bindsRentalAgreementsRepository(
        rentalAgreementsRepository: OfflineFirstRentalAgreementsRepository,
    ): RentalAgreementsRepository

    @Binds
    internal abstract fun bindsRentalInvitesRepository(
        rentalInvitesRepository: OfflineFirstRentalInvitesRepository,
    ): RentalInvitesRepository

    @Binds
    internal abstract fun bindsRentalOffersRepository(
        rentalOffersRepository: OfflineFirstRentalOffersRepository,
    ): RentalOffersRepository

    // Other
    @Binds
    internal abstract fun bindsNetworkMonitor(
        networkMonitor: ConnectivityManagerNetworkMonitor,
    ): NetworkMonitor

    @Binds
    internal abstract fun binds(impl: TimeZoneBroadcastMonitor): TimeZoneMonitor
}