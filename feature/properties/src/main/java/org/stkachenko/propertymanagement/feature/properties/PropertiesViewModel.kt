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
import org.stkachenko.propertymanagement.core.data.util.SyncManager
import org.stkachenko.propertymanagement.core.domain.property.GetPropertiesByOwnerIdUseCase
import org.stkachenko.propertymanagement.core.ui.property.PropertiesUiState
import org.stkachenko.propertymanagement.core.ui.property.UserRoleState
import javax.inject.Inject

@HiltViewModel
class PropertiesViewModel @Inject constructor(
    syncManager: SyncManager,
    userSessionRepository: UserSessionRepository,
    getProperties: GetPropertiesByOwnerIdUseCase,
) : ViewModel() {

    val isSyncing = syncManager.isSyncing
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    val userRole: StateFlow<UserRoleState> = userSessionRepository.userSessionData
        .map { UserRoleState.Success(it.userRole) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UserRoleState.Loading,
        )

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

//fun followTopicToggle(followed: Boolean) {
//    viewModelScope.launch {
//        userDataRepository.setTopicIdFollowed(topicArgs.topicId, followed)
//    }
//}