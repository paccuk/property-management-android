package org.stkachenko.propertymanagement.core.data.model.rental

import org.stkachenko.propertymanagement.core.database.model.rental.RentalInviteEntity
import org.stkachenko.propertymanagement.core.network.model.rental.NetworkRentalInvite

fun NetworkRentalInvite.asEntity() = RentalInviteEntity(
    id = id,
    offerId = offerId,
    inviteType = inviteType,
    inviteToken = inviteToken,
    targetContact = targetContact,
    status = status,
    usedAt = usedAt,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun RentalInviteEntity.asNetworkModel() = NetworkRentalInvite(
    id = id,
    offerId = offerId,
    inviteType = inviteType,
    inviteToken = inviteToken,
    targetContact = targetContact,
    status = status,
    usedAt = usedAt,
    createdAt = createdAt,
    updatedAt = updatedAt,
)