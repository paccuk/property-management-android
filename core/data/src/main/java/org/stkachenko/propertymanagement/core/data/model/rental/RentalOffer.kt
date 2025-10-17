package org.stkachenko.propertymanagement.core.data.model.rental

import org.stkachenko.propertymanagement.core.database.model.rental.RentalOfferEntity
import org.stkachenko.propertymanagement.core.network.model.rental.NetworkRentalOffer
import java.util.Date

fun NetworkRentalOffer.asEntity() = RentalOfferEntity(
    id = id,
    propertyId = propertyId,
    ownerId = ownerId,
    availableFrom = Date(availableFrom),
    availableTo = Date(availableTo),
    minDuration = minDuration,
    maxDuration = maxDuration,
    maxTenants = naxTenants,
    isActive = isActive,
    pricePerPerson = pricePerPerson,
    currency = currency,
    termsText = termsText,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun RentalOfferEntity.asNetworkModel() = NetworkRentalOffer(
    id = id,
    propertyId = propertyId,
    ownerId = ownerId,
    availableFrom = availableFrom.time,
    availableTo = availableTo.time,
    minDuration = minDuration,
    maxDuration = maxDuration,
    naxTenants = maxTenants,
    isActive = isActive,
    pricePerPerson = pricePerPerson,
    currency = currency,
    termsText = termsText,
    createdAt = createdAt,
    updatedAt = updatedAt,
)