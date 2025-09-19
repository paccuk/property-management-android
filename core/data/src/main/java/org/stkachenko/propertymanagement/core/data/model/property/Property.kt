package org.stkachenko.propertymanagement.core.data.model.property

import org.stkachenko.propertymanagement.core.database.model.property.PropertyEntity
import org.stkachenko.propertymanagement.core.network.model.property.NetworkProperty


fun NetworkProperty.asEntity() = PropertyEntity(
    id = id,
    ownerId = ownerId,
    price = price,
    currency = currency,
    type = type,
    area = area,
    isAvailable = isAvailable,
    address = address,
    attributes = attributes,
    createdAt = createdAt,
    updatedAt = updatedAt,
)