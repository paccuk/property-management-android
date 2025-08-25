package org.stkachenko.propertymanagement.core.datastore

data class ChangeListVersions(
    // User
    val userVersion: Int = -1,
    val profileVersion: Int = -1,

    // Property
    val propertyVersion: Int = -1,

    // Chat
    val chatVersion: Int = -1,
    val chatParticipantVersion: Int = -1,
    val messageVersion: Int = -1,

    // Payment
    val paymentVersion: Int = -1,
    val invoiceVersion: Int = -1,
    val paymentScheduleVersion: Int = -1,

    // Rental
    val rentalAgreementVersion: Int = -1,
    val rentalInviteVersion: Int = -1,
    val rentalOfferVersion: Int = -1,

    // Other
    val imageVersion: Int = -1,
)