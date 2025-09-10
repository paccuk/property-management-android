package org.stkachenko.propertymanagement.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.stkachenko.propertymanagement.core.data.repository.userdata.UserDataRepository
import org.stkachenko.propertymanagement.core.data.repository.usersession.UserSessionRepository
import org.stkachenko.propertymanagement.core.data.util.SyncManager
import org.stkachenko.propertymanagement.core.domain.property.GetPropertiesUseCase
import org.stkachenko.propertymanagement.core.model.data.user.UserRole
import org.stkachenko.propertymanagement.core.ui.PropertiesUiState
import org.stkachenko.propertymanagement.feature.home.model.HomeCategory
import org.stkachenko.propertymanagement.feature.home.model.HomeCategory.Companion.getAllCategories
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    syncManager: SyncManager,
    private val userDataRepository: UserDataRepository,
    userSessionRepository: UserSessionRepository,
    getProperties: GetPropertiesUseCase,
) : ViewModel() {

    val isSyncing = syncManager.isSyncing
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    val propertiesState: StateFlow<PropertiesUiState> =
        getProperties("") // TODO("Add proper ownerId")
            .map(PropertiesUiState::Success)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = PropertiesUiState.Loading,
            )

    val userRole: StateFlow<UserRole> = userSessionRepository.userRole
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UserRole.TENANT, // TODO("Add proper initial value")
        )

    private val _categoriesState = MutableStateFlow<CategoriesUiState>(CategoriesUiState.Loading)
    val categoriesState: StateFlow<CategoriesUiState> = _categoriesState

    init {
        _categoriesState.value = CategoriesUiState.Success(getAllCategories())
    }
}