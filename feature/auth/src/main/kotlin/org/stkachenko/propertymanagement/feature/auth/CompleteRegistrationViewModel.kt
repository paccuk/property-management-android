package org.stkachenko.propertymanagement.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.stkachenko.propertymanagement.core.domain.user.CompleteUserProfileUseCase
import org.stkachenko.propertymanagement.core.ui.auth.CompleteRegistrationUiState
import javax.inject.Inject

@HiltViewModel
class CompleteRegistrationViewModel @Inject constructor(
    private val completeProfile: CompleteUserProfileUseCase,
) : ViewModel() {
    private val _completeRegistrationState =
        MutableStateFlow<CompleteRegistrationUiState>(CompleteRegistrationUiState.Idle)
    val completeRegistrationState: StateFlow<CompleteRegistrationUiState> =
        _completeRegistrationState.asStateFlow()

    fun completeRegistration(
        firstName: String,
        lastName: String,
        phoneNumber: String,
        role: String,
        avatarImageUrl: String?,
    ) {
        viewModelScope.launch {
            try {
                completeProfile(firstName, lastName, phoneNumber, role, avatarImageUrl)
                _completeRegistrationState.value = CompleteRegistrationUiState.Success
            } catch (e: Exception) {
                _completeRegistrationState.value =
                    CompleteRegistrationUiState.Error(e.message ?: "Unknown error.")
            }
        }
    }
}

