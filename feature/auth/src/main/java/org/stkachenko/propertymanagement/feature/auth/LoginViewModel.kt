package org.stkachenko.propertymanagement.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.stkachenko.propertymanagement.core.data.repository.auth.AuthRepository
import org.stkachenko.propertymanagement.core.data.repository.usersession.UserSessionRepository
import org.stkachenko.propertymanagement.core.data.util.SyncManager
import org.stkachenko.propertymanagement.core.domain.auth.AuthenticateUserUseCase
import org.stkachenko.propertymanagement.core.ui.auth.LoginUiState
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    syncManager: SyncManager,
    private val userSessionRepository: UserSessionRepository,
    private val authenticateUser: AuthenticateUserUseCase,
) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginState: StateFlow<LoginUiState> = _loginState.asStateFlow()

    val isSyncing = syncManager.isSyncing
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.update { LoginUiState.Loading }
            runCatching { authenticateUser(email, password) }
                .onSuccess { userId ->
                    userSessionRepository.setIsLoggedIn(true)
                    userSessionRepository.setUserId(userId.id)
                    userSessionRepository.setUserRole(userId.role)

                    _loginState.update { LoginUiState.Success }
                }
                .onFailure { e ->
                    _loginState.update { LoginUiState.Error(e.message ?: "Unknown error") }
                }
        }
    }
}