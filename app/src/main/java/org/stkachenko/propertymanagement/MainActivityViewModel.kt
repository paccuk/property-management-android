package org.stkachenko.propertymanagement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import org.stkachenko.propertymanagement.core.data.repository.userdata.UserDataRepository
import org.stkachenko.propertymanagement.MainActivityUiState.Loading
import org.stkachenko.propertymanagement.MainActivityUiState.Success
import org.stkachenko.propertymanagement.core.data.repository.usersession.UserSessionRepository
import org.stkachenko.propertymanagement.core.model.data.userdata.UserData
import org.stkachenko.propertymanagement.core.model.data.user.UserRole
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    userSessionRepository: UserSessionRepository,
    userDataRepository: UserDataRepository,
) : ViewModel() {
    val uiState: StateFlow<MainActivityUiState> = combine(
        userSessionRepository.userRole,
        userDataRepository.userData,
    ) { userRole, userData ->
        Success(userRole = userRole, userData = userData)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = Loading,
    )
}

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data class Success(
        val userRole: UserRole,
        val userData: UserData,
    ) : MainActivityUiState
}