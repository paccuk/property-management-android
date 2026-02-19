package org.stkachenko.propertymanagement.feature.properties

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.stkachenko.propertymanagement.core.data.repository.usersession.UserSessionRepository
import org.stkachenko.propertymanagement.core.domain.property.GetPropertiesByOwnerIdUseCase
import org.stkachenko.propertymanagement.core.ui.property.PropertiesUiState
import javax.inject.Inject

@HiltViewModel
class PropertiesViewModel @Inject constructor(
    userSessionRepository: UserSessionRepository,
    getProperties: GetPropertiesByOwnerIdUseCase,
) : ViewModel() {
    val propertiesState: StateFlow<PropertiesUiState> =
        userSessionRepository.userSessionData
            .map { it.userId }
            .filter { it.isNotEmpty() }
            .flatMapLatest { userId ->
                getProperties(userId).map(PropertiesUiState::Success)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = PropertiesUiState.Loading,
            )
}