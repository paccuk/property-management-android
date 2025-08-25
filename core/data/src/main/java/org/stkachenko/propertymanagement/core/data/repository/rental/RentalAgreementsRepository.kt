package org.stkachenko.propertymanagement.core.data.repository.rental

import kotlinx.coroutines.flow.Flow
import org.stkachenko.propertymanagement.core.data.Syncable
import org.stkachenko.propertymanagement.core.model.data.rental.RentalAgreement

interface RentalAgreementsRepository : Syncable {
    fun getRentalAgreementsByOfferId(id: String): Flow<List<RentalAgreement>>
}

