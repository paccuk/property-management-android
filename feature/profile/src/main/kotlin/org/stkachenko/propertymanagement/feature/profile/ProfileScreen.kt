package org.stkachenko.propertymanagement.feature.profile

import androidx.activity.compose.ReportDrawnWhen
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.stkachenko.propertymanagement.core.designsystem.component.PmOverlayBuildingHouseLoading
import org.stkachenko.propertymanagement.core.model.data.userdata.DarkThemeConfig
import org.stkachenko.propertymanagement.core.ui.profile.LocaleUiState
import org.stkachenko.propertymanagement.core.ui.profile.ProfileContent
import org.stkachenko.propertymanagement.core.ui.profile.ProfileUiState
import org.stkachenko.propertymanagement.core.ui.profile.SettingsUiState

@Composable
internal fun ProfileRoute(
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val profileState by viewModel.profileUiState.collectAsStateWithLifecycle()
    val settingsUiState by viewModel.settingsUiState.collectAsStateWithLifecycle()
    val localeUiState by viewModel.localeUiState.collectAsStateWithLifecycle()
    val isLoggedOut by viewModel.isLoggedOut.collectAsStateWithLifecycle()

    LaunchedEffect(isLoggedOut) {
        if (isLoggedOut) {
            onNavigateToLogin()
        }
    }

    ProfileScreen(
        profileState = profileState,
        settingsUiState = settingsUiState,
        localeUiState = localeUiState,
        onEditProfileClick = viewModel::updateUserProfile,
        onChangeDarkThemeConfig = viewModel::updateDarkThemeConfig,
        onLanguageChange = viewModel::changeLanguage,
        onLogout = viewModel::logout,
        onChangePassword = viewModel::changePassword,
        modifier = modifier,
    )
}

@Composable
fun ProfileScreen(
    profileState: ProfileUiState,
    settingsUiState: SettingsUiState,
    localeUiState: LocaleUiState,
    onEditProfileClick: (String, String, String, String) -> Unit,
    modifier: Modifier = Modifier,
    onChangeDarkThemeConfig: (darkThemeConfig: DarkThemeConfig) -> Unit,
    onLanguageChange: (String) -> Unit,
    onChangePassword: (String, String, String) -> Unit,
    onLogout: () -> Unit,
) {
    val isProfileLoading = profileState is ProfileUiState.Loading

    ReportDrawnWhen { !isProfileLoading }

    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        ProfileContent(
            profileState = profileState,
            settingsUiState = settingsUiState,
            localeUiState = localeUiState,
            onEditProfileClick = onEditProfileClick,
            onChangeDarkThemeConfig = onChangeDarkThemeConfig,
            onLanguageChange = onLanguageChange,
            onChangePassword = onChangePassword,
            onLogout = onLogout,
        )

        AnimatedVisibility(
            visible = isProfileLoading,
            enter = slideInVertically(
                initialOffsetY = { fullHeight -> -fullHeight },
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { fullHeight -> -fullHeight },
            ) + fadeOut(),
        ) {
            val loadingContentDescriptor =
                stringResource(R.string.feature_profile_profile_loading_content_description)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            ) {
                PmOverlayBuildingHouseLoading(
                    modifier = Modifier
                        .align(Alignment.Center),
                    contentDesc = loadingContentDescriptor,
                )
            }
        }
    }
}
