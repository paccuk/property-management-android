package org.stkachenko.propertymanagement.core.testing.network

import org.stkachenko.propertymanagement.core.network.ProtectedNetworkDataSource
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
import org.stkachenko.propertymanagement.core.network.model.user.ChangePasswordRequest
import org.stkachenko.propertymanagement.core.network.model.user.CompleteUserProfileRequest
import org.stkachenko.propertymanagement.core.network.model.user.NetworkUser
import org.stkachenko.propertymanagement.core.network.model.user.UpdateUserProfileRequest

class TestProtectedNetworkDataSource : ProtectedNetworkDataSource {

    private val properties = mutableListOf<NetworkProperty>()
    private val users = mutableListOf<NetworkUser>()

    var updatedProperties: List<NetworkProperty> = emptyList()
        private set

    // Property
    override suspend fun getProperties(ids: List<String>?): List<NetworkProperty> =
        if (ids == null) properties else properties.filter { it.id in ids }

    override suspend fun getPropertiesUpdatedAfter(timestamp: Long): List<NetworkProperty> =
        properties.filter { it.updatedAt > timestamp }

    override suspend fun createProperties(properties: List<NetworkProperty>): List<NetworkProperty> {
        this.properties.addAll(properties)
        return properties
    }

    override suspend fun updateProperties(properties: List<NetworkProperty>): List<NetworkProperty> {
        updatedProperties = properties
        return properties
    }

    override suspend fun deleteProperty(ids: List<String>) {
        properties.removeAll { it.id in ids }
    }

    fun setProperties(vararg property: NetworkProperty) {
        properties.clear()
        properties.addAll(property)
    }

    // User - stub implementations
    override suspend fun getUsers(ids: List<String>?): List<NetworkUser> =
        if (ids == null) users else users.filter { it.id in ids }

    override suspend fun getUsersUpdatedAfter(timestamp: Long): List<NetworkUser> =
        users.filter { it.updatedAt > timestamp }

    override suspend fun createUsers(users: List<NetworkUser>): List<NetworkUser> {
        this.users.addAll(users)
        return users
    }

    override suspend fun updateUser(updateUserProfileRequest: UpdateUserProfileRequest): NetworkUser =
        throw NotImplementedError()

    override suspend fun updateUsers(users: List<NetworkUser>): List<NetworkUser> =
        throw NotImplementedError()

    override suspend fun deleteUsers(ids: List<String>) {
        this.users.removeAll { it.id in ids }
    }

    override suspend fun completeUserProfile(completeUserProfileRequest: CompleteUserProfileRequest): NetworkUser =
        throw NotImplementedError()

    override suspend fun getUserByToken(): NetworkUser =
        throw NotImplementedError()

    override suspend fun changePassword(changePasswordRequest: ChangePasswordRequest): NetworkUser =
        throw NotImplementedError()

    // Chat - stub implementations
    override suspend fun getChats(ids: List<String>?): List<NetworkChat> = emptyList()
    override suspend fun getChatParticipants(ids: List<String>?): List<NetworkChatParticipant> = emptyList()
    override suspend fun getMessages(ids: List<String>?): List<NetworkMessage> = emptyList()

    // Payment - stub implementations
    override suspend fun getPayments(ids: List<String>?): List<NetworkPayment> = emptyList()
    override suspend fun getInvoices(ids: List<String>?): List<NetworkInvoice> = emptyList()
    override suspend fun getPaymentSchedules(ids: List<String>?): List<NetworkPaymentSchedule> = emptyList()
    override suspend fun getPaymentsUpdatedAfter(timestamp: Long): List<NetworkPayment> = emptyList()
    override suspend fun getInvoicesUpdatedAfter(timestamp: Long): List<NetworkInvoice> = emptyList()
    override suspend fun getPaymentSchedulesUpdatedAfter(timestamp: Long): List<NetworkPaymentSchedule> = emptyList()
    override suspend fun createPayments(payments: List<NetworkPayment>): List<NetworkPayment> = payments
    override suspend fun updatePayments(payments: List<NetworkPayment>): List<NetworkPayment> = payments
    override suspend fun deletePayments(ids: List<String>) {}
    override suspend fun createInvoices(invoices: List<NetworkInvoice>): List<NetworkInvoice> = invoices
    override suspend fun updateInvoices(invoices: List<NetworkInvoice>): List<NetworkInvoice> = invoices
    override suspend fun deleteInvoices(ids: List<String>) {}
    override suspend fun createPaymentSchedules(paymentSchedules: List<NetworkPaymentSchedule>): List<NetworkPaymentSchedule> = paymentSchedules
    override suspend fun updatePaymentSchedules(paymentSchedules: List<NetworkPaymentSchedule>): List<NetworkPaymentSchedule> = paymentSchedules
    override suspend fun deletePaymentSchedules(ids: List<String>) {}

    // Rental - stub implementations
    override suspend fun getRentalAgreements(ids: List<String>?): List<NetworkRentalAgreement> = emptyList()
    override suspend fun getRentalInvites(ids: List<String>?): List<NetworkRentalInvite> = emptyList()
    override suspend fun getRentalOffers(ids: List<String>?): List<NetworkRentalOffer> = emptyList()
    override suspend fun getRentalAgreementsUpdatedAfter(timestamp: Long): List<NetworkRentalAgreement> = emptyList()
    override suspend fun getRentalInvitesUpdatedAfter(timestamp: Long): List<NetworkRentalInvite> = emptyList()
    override suspend fun getRentalOffersUpdatedAfter(timestamp: Long): List<NetworkRentalOffer> = emptyList()
    override suspend fun createRentalAgreements(rentalAgreements: List<NetworkRentalAgreement>): List<NetworkRentalAgreement> = rentalAgreements
    override suspend fun updateRentalAgreements(rentalAgreements: List<NetworkRentalAgreement>): List<NetworkRentalAgreement> = rentalAgreements
    override suspend fun deleteRentalAgreements(ids: List<String>) {}
    override suspend fun createRentalInvites(rentalInvites: List<NetworkRentalInvite>): List<NetworkRentalInvite> = rentalInvites
    override suspend fun updateRentalInvites(rentalInvites: List<NetworkRentalInvite>): List<NetworkRentalInvite> = rentalInvites
    override suspend fun deleteRentalInvites(ids: List<String>) {}
    override suspend fun createRentalOffers(rentalOffers: List<NetworkRentalOffer>): List<NetworkRentalOffer> = rentalOffers
    override suspend fun updateRentalOffers(rentalOffers: List<NetworkRentalOffer>): List<NetworkRentalOffer> = rentalOffers
    override suspend fun deleteRentalOffers(ids: List<String>) {}
}