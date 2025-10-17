package org.stkachenko.propertymanagement.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.stkachenko.propertymanagement.core.data.repository.user.UserRepository
import org.stkachenko.propertymanagement.core.data.repository.userdata.UserDataRepository
import org.stkachenko.propertymanagement.core.data.repository.usersession.UserSessionRepository
import org.stkachenko.propertymanagement.core.domain.user.GetUserByIdUseCase
import org.stkachenko.propertymanagement.core.domain.user.UpdateUserUseCase
import org.stkachenko.propertymanagement.core.model.data.userdata.DarkThemeConfig
import org.stkachenko.propertymanagement.core.ui.profile.ProfileUiState
import org.stkachenko.propertymanagement.core.ui.profile.SettingsUiState
import org.stkachenko.propertymanagement.core.ui.profile.UserEditableSettings
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val updateUser: UpdateUserUseCase,
    getUser: GetUserByIdUseCase,
    userSessionRepository: UserSessionRepository,
) : ViewModel() {

    val profileUiState: StateFlow<ProfileUiState> =
        userSessionRepository.userSessionData
            .flatMapLatest { userSessionData ->
                if (userSessionData.userId.isNotBlank()) {
                    getUser(userSessionData.userId)
                } else {
                    flowOf(null)
                }
            }
            .map { user ->
                if (user != null) {
                    ProfileUiState.Success(user)
                } else {
                    ProfileUiState.Error("User not found or not logged in")
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = WhileSubscribed(5.seconds.inWholeMilliseconds),
                initialValue = ProfileUiState.Loading
            )

    val settingsUiState: StateFlow<SettingsUiState> =
        userDataRepository.userData
            .map { userData ->
                SettingsUiState.Success(
                    settings = UserEditableSettings(
                        useDynamicColor = userData.useDynamicColor,
                        darkThemeConfig = userData.darkThemeConfig,
                    ),
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = WhileSubscribed(5.seconds.inWholeMilliseconds),
                initialValue = SettingsUiState.Loading
            )

    fun updateUserProfile(
        firstName: String,
        lastName: String,
        phone: String,
        avatarImageUrl: String,
    ) {
        viewModelScope.launch {
            try {
                updateUser(
                    firstName = firstName,
                    lastName = lastName,
                    phone = phone,
                    avatarImageUrl = avatarImageUrl,
                )
            } catch (e: Exception) {
                ProfileUiState.Error("Unexpected  error.")
            }
        }
    }

    fun updateDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        viewModelScope.launch {
            userDataRepository.setDarkThemeConfig(darkThemeConfig)
        }
    }
}