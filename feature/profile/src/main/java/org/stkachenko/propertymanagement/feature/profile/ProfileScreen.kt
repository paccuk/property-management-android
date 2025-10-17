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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.stkachenko.propertymanagement.core.designsystem.component.PmOverlayBuildingHouseLoading
import org.stkachenko.propertymanagement.core.model.data.userdata.DarkThemeConfig
import org.stkachenko.propertymanagement.core.ui.profile.ProfileContent
import org.stkachenko.propertymanagement.core.ui.profile.ProfileUiState
import org.stkachenko.propertymanagement.core.ui.profile.SettingsUiState

@Composable
internal fun ProfileRoute(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val profileState by viewModel.profileUiState.collectAsStateWithLifecycle()
    val settingsUiState by viewModel.settingsUiState.collectAsStateWithLifecycle()

    ProfileScreen(
        profileState = profileState,
        settingsUiState = settingsUiState,
        onEditProfileClick = viewModel::updateUserProfile,
        onChangeDarkThemeConfig = viewModel::updateDarkThemeConfig,
        onLanguageChangeClick = viewModel::onLanguageChangeClick,
        onLogoutClick = viewModel::onLogoutClick,
        modifier = modifier,
    )
}

@Composable
fun ProfileScreen(
    profileState: ProfileUiState,
    settingsUiState: SettingsUiState,
    onEditProfileClick: (String, String, String, String) -> Unit,
    modifier: Modifier = Modifier,
    onChangeDarkThemeConfig: (darkThemeConfig: DarkThemeConfig) -> Unit,
    onLanguageChangeClick: () -> Unit,
    onLogoutClick: () -> Unit,
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
            onEditProfileClick = onEditProfileClick,
            onChangeDarkThemeConfig = onChangeDarkThemeConfig,
            onLanguageChangeClick = onLanguageChangeClick,
            onLogoutClick = onLogoutClick,
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
