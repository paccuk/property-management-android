package org.stkachenko.propertymanagement.core.network

import org.stkachenko.propertymanagement.core.network.model.NetworkChangeList
import org.stkachenko.propertymanagement.core.network.model.auth.AuthResponse
import org.stkachenko.propertymanagement.core.network.model.chat.NetworkChat
import org.stkachenko.propertymanagement.core.network.model.chat.NetworkChatParticipant
import org.stkachenko.propertymanagement.core.network.model.chat.NetworkMessage
import org.stkachenko.propertymanagement.core.network.model.image.NetworkImage
import org.stkachenko.propertymanagement.core.network.model.payment.NetworkInvoice
import org.stkachenko.propertymanagement.core.network.model.payment.NetworkPayment
import org.stkachenko.propertymanagement.core.network.model.payment.NetworkPaymentSchedule
import org.stkachenko.propertymanagement.core.network.model.profile.NetworkProfile
import org.stkachenko.propertymanagement.core.network.model.property.NetworkProperty
import org.stkachenko.propertymanagement.core.network.model.rental.NetworkRentalAgreement
import org.stkachenko.propertymanagement.core.network.model.rental.NetworkRentalInvite
import org.stkachenko.propertymanagement.core.network.model.rental.NetworkRentalOffer
import org.stkachenko.propertymanagement.core.network.model.user.NetworkUser

interface PmNetworkDataSource {
    // User
    suspend fun getUsers(ids: List<String>? = null): List<NetworkUser>
    suspend fun getProfiles(ids: List<String>? = null): List<NetworkProfile>
    suspend fun getUserChangeList(after: Int? = null): List<NetworkChangeList>
    suspend fun getProfileChangeList(after: Int? = null): List<NetworkChangeList>

    // Property
    suspend fun getProperties(ids: List<String>? = null): List<NetworkProperty>
    suspend fun getPropertyChangeList(after: Int? = null): List<NetworkChangeList>

    // Chat
    suspend fun getChats(ids: List<String>? = null): List<NetworkChat>
    suspend fun getChatParticipants(ids: List<String>? = null): List<NetworkChatParticipant>
    suspend fun getMessages(ids: List<String>? = null): List<NetworkMessage>
    suspend fun getChatChangeList(after: Int? = null): List<NetworkChangeList>
    suspend fun getChatParticipantChangeList(after: Int? = null): List<NetworkChangeList>
    suspend fun getMessageChangeList(after: Int? = null): List<NetworkChangeList>

    // Payment
    suspend fun getPayments(ids: List<String>? = null): List<NetworkPayment>
    suspend fun getInvoices(ids: List<String>? = null): List<NetworkInvoice>
    suspend fun getPaymentSchedules(ids: List<String>? = null): List<NetworkPaymentSchedule>
    suspend fun getPaymentChangeList(after: Int? = null): List<NetworkChangeList>
    suspend fun getInvoiceChangeList(after: Int? = null): List<NetworkChangeList>
    suspend fun getPaymentScheduleChangeList(after: Int? = null): List<NetworkChangeList>

    // Rental
    suspend fun getRentalAgreements(ids: List<String>? = null): List<NetworkRentalAgreement>
    suspend fun getRentalInvites(ids: List<String>? = null): List<NetworkRentalInvite>
    suspend fun getRentalOffers(ids: List<String>? = null): List<NetworkRentalOffer>
    suspend fun getRentalAgreementChangeList(after: Int? = null): List<NetworkChangeList>
    suspend fun getRentalInviteChangeList(after: Int? = null): List<NetworkChangeList>
    suspend fun getRentalOfferChangeList(after: Int? = null): List<NetworkChangeList>

    // Other
    suspend fun getImages(ids: List<String>? = null): List<NetworkImage>
    suspend fun getImageChangeList(after: Int? = null): List<NetworkChangeList>
}