package org.stkachenko.propertymanagement.core.data.repository.rental

import kotlinx.coroutines.flow.Flow
import org.stkachenko.propertymanagement.core.data.Syncable
import org.stkachenko.propertymanagement.core.model.data.rental.RentalOffer

interface RentalOffersRepository: Syncable {
    fun getRentalOfferEntitiesByOwnerId(id: String): Flow<List<RentalOffer>>
    fun getRentalOfferEntitiesByPropertyId(id: String): Flow<List<RentalOffer>>
}

