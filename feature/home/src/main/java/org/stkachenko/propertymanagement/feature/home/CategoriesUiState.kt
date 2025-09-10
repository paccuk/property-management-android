package org.stkachenko.propertymanagement.feature.home

import org.stkachenko.propertymanagement.feature.home.model.HomeCategory

sealed interface CategoriesUiState {

    data object Loading : CategoriesUiState

    data class Success(val categories: List<HomeCategory>) : CategoriesUiState
}