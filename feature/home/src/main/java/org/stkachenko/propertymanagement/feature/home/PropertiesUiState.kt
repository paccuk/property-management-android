package org.stkachenko.propertymanagement.feature.home

import org.stkachenko.propertymanagement.core.model.data.property.Property

sealed interface PropertiesUiState {
    data object Loading : PropertiesUiState
    data class Success(val properties: List<Property>) : PropertiesUiState
}