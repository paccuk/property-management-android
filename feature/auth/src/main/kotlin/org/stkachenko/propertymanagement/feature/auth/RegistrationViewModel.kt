package org.stkachenko.propertymanagement.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.stkachenko.propertymanagement.core.data.repository.auth.AuthRepository
import org.stkachenko.propertymanagement.core.ui.auth.RegistrationUiState
import javax.inject.Inject

private const val ERROR_MESSAGE = "Unknown error."

private const val PASSWORDS_DO_NOT_MATCH_MESSAGE = "Passwords do not match."

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _registrationState = MutableStateFlow<RegistrationUiState>(RegistrationUiState.Idle)
    val registrationState: StateFlow<RegistrationUiState> = _registrationState.asStateFlow()

    fun register(email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            if (password != confirmPassword) {
                _registrationState.value = RegistrationUiState.Error(PASSWORDS_DO_NOT_MATCH_MESSAGE)
                return@launch
            }
            try {
                authRepository.register(email, password)
                _registrationState.value = RegistrationUiState.TokenReceived
            } catch (e: Exception) {
                _registrationState.value =
                    RegistrationUiState.Error(e.message ?: ERROR_MESSAGE)
            }
        }
    }
}