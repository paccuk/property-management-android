package org.stkachenko.propertymanagement.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.stkachenko.propertymanagement.core.data.repository.auth.AuthRepository
import org.stkachenko.propertymanagement.core.data.repository.usersession.UserSessionRepository
import org.stkachenko.propertymanagement.core.ui.auth.LoginUiState
import javax.inject.Inject

private const val ERROR_MESSAGE = "Unknown error."

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginState: StateFlow<LoginUiState> = _loginState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginUiState.Loading

            try {
                authRepository.login(email, password)
                _loginState.value = LoginUiState.Success

            } catch (e: Exception) {
                _loginState.value = LoginUiState.Error(e.message ?: ERROR_MESSAGE)
            }
        }
    }
}