package org.stkachenko.propertymanagement.core.domain.property

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.stkachenko.propertymanagement.core.data.repository.property.PropertiesRepository
import org.stkachenko.propertymanagement.core.domain.property.PropertySortField.NONE
import org.stkachenko.propertymanagement.core.model.data.property.Property
import javax.inject.Inject

class GetPropertiesByOwnerIdUseCase @Inject constructor(
    private val propertiesRepository: PropertiesRepository,
) {
    operator fun invoke(ownerId: String, sortBy: PropertySortField = NONE): Flow<List<Property>> =
            propertiesRepository.getPropertiesByOwnerId(ownerId).map { properties ->
            when (sortBy) {
                PropertySortField.PRICE -> properties.sortedBy { it.price }
                PropertySortField.AREA -> properties.sortedBy { it.area }
                else -> properties
            }
        }
}

enum class PropertySortField {
    NONE,
    PRICE,
    AREA,
}