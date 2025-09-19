package org.stkachenko.propertymanagement.core.domain.property

import kotlinx.coroutines.flow.Flow
import org.stkachenko.propertymanagement.core.data.repository.property.PropertiesRepository
import org.stkachenko.propertymanagement.core.model.data.property.Property
import javax.inject.Inject

class GetPropertyByIdUseCase @Inject constructor(
    private val propertiesRepository: PropertiesRepository,
) {
    operator fun invoke(propertyId: String): Flow<Property?> =
        propertiesRepository.getPropertyById(propertyId)
}