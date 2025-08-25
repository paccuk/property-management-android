package org.stkachenko.propertymanagement.core.data.model.rental

import org.stkachenko.propertymanagement.core.database.model.rental.RentalAgreementEntity
import org.stkachenko.propertymanagement.core.network.model.rental.NetworkRentalAgreement
import java.util.Date

fun NetworkRentalAgreement.asEntity() = RentalAgreementEntity(
    id = id,
    offerId = offerId,
    tenantId = tenantId,
    startDate = Date(startDate),
    endDate = Date(endDate),
    status = status,
    createdAt = createdAt,
    updatedAt = updatedAt,
)