package org.stkachenko.propertymanagement.core.domain.property

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.stkachenko.propertymanagement.core.data.repository.property.PropertiesRepository
import org.stkachenko.propertymanagement.core.data.repository.userdata.UserDataRepository
import org.stkachenko.propertymanagement.core.domain.property.PropertySortField.NONE
import org.stkachenko.propertymanagement.core.model.data.property.Property
import javax.inject.Inject

class GetPropertiesUseCase @Inject constructor(
    private val propertiesRepository: PropertiesRepository,
    private val userDataRepository: UserDataRepository,
) {
    operator fun invoke(ownerId: String, sortBy: PropertySortField = NONE): Flow<List<Property>> =
        combine(
            userDataRepository.userData,
            propertiesRepository.getPropertiesByOwnerId(ownerId),
        ) { userData, properties ->
            val favoriteProperties = properties
//                .map { property ->
//                    property.copy(isFavorite = property.id in userData.favoriteProperties)
//                }
            when (sortBy) {
                PropertySortField.PRICE -> favoriteProperties.sortedBy { it.price }
                PropertySortField.AREA -> favoriteProperties.sortedBy { it.area }
                else -> favoriteProperties
            }
        }
}

enum class PropertySortField {
    NONE,
    PRICE,
    AREA,
}
