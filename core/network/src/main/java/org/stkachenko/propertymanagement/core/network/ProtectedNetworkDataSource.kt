package org.stkachenko.propertymanagement.core.network

import org.stkachenko.propertymanagement.core.network.model.NetworkChangeList
import org.stkachenko.propertymanagement.core.network.model.chat.NetworkChat
import org.stkachenko.propertymanagement.core.network.model.chat.NetworkChatParticipant
import org.stkachenko.propertymanagement.core.network.model.chat.NetworkMessage
import org.stkachenko.propertymanagement.core.network.model.payment.NetworkInvoice
import org.stkachenko.propertymanagement.core.network.model.payment.NetworkPayment
import org.stkachenko.propertymanagement.core.network.model.payment.NetworkPaymentSchedule
import org.stkachenko.propertymanagement.core.network.model.property.NetworkProperty
import org.stkachenko.propertymanagement.core.network.model.rental.NetworkRentalAgreement
import org.stkachenko.propertymanagement.core.network.model.rental.NetworkRentalInvite
import org.stkachenko.propertymanagement.core.network.model.rental.NetworkRentalOffer
import org.stkachenko.propertymanagement.core.network.model.user.NetworkUser

interface ProtectedNetworkDataSource {
    // User
    suspend fun getUsers(ids: List<String>? = null): List<NetworkUser>
    suspend fun getUserChangeList(after: Int? = null): List<NetworkChangeList>

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
    suspend fun createUsers(users: List<NetworkUser>): List<NetworkUser>
    suspend fun updateUsers(users: List<NetworkUser>): List<NetworkUser>
    suspend fun deleteUsers(ids: List<String>)
    suspend fun createProperties(properties: List<NetworkProperty>): List<NetworkProperty>
    suspend fun updateProperties(properties: List<NetworkProperty>): List<NetworkProperty>
    suspend fun deleteProperty(ids: List<String>)
    suspend fun createPayments(payments: List<NetworkPayment>): List<NetworkPayment>
    suspend fun updatePayments(payments: List<NetworkPayment>): List<NetworkPayment>
    suspend fun deletePayments(ids: List<String>)
    suspend fun createInvoices(invoices: List<NetworkInvoice>): List<NetworkInvoice>
    suspend fun updateInvoices(invoices: List<NetworkInvoice>): List<NetworkInvoice>
    suspend fun deleteInvoices(ids: List<String>)
    suspend fun createPaymentSchedules(paymentSchedules: List<NetworkPaymentSchedule>): List<NetworkPaymentSchedule>
    suspend fun updatePaymentSchedules(paymentSchedules: List<NetworkPaymentSchedule>): List<NetworkPaymentSchedule>
    suspend fun deletePaymentSchedules(ids: List<String>)
    suspend fun createRentalAgreements(rentalAgreements: List<NetworkRentalAgreement>): List<NetworkRentalAgreement>
    suspend fun updateRentalAgreements(rentalAgreements: List<NetworkRentalAgreement>): List<NetworkRentalAgreement>
    suspend fun deleteRentalAgreements(ids: List<String>)
    suspend fun createRentalInvites(rentalInvites: List<NetworkRentalInvite>): List<NetworkRentalInvite>
    suspend fun updateRentalInvites(rentalInvites: List<NetworkRentalInvite>): List<NetworkRentalInvite>
    suspend fun deleteRentalInvites(ids: List<String>)
    suspend fun createRentalOffers(rentalOffers: List<NetworkRentalOffer>): List<NetworkRentalOffer>
    suspend fun updateRentalOffers(rentalOffers: List<NetworkRentalOffer>): List<NetworkRentalOffer>
    suspend fun deleteRentalOffers(ids: List<String>)
}