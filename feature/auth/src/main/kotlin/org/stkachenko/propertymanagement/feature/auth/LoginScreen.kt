package org.stkachenko.propertymanagement.feature.auth

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
import org.stkachenko.propertymanagement.core.designsystem.theme.PmTheme
import org.stkachenko.propertymanagement.core.ui.DevicePreviews
import org.stkachenko.propertymanagement.core.ui.auth.LoginContent
import org.stkachenko.propertymanagement.core.ui.auth.LoginUiState


@Composable
internal fun LoginRoute(
    onLoginSuccess: () -> Unit,
    onNavigateToRegistration: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val loginState by viewModel.loginState.collectAsStateWithLifecycle()

    LoginScreen(
        onLoginClick = { email, password ->
            viewModel.login(email, password)
        },
        loginState = loginState,
        onLoginSuccess = onLoginSuccess,
        onNavigateToRegistration = onNavigateToRegistration,
        modifier = modifier,
    )
}

@Composable
fun LoginScreen(
    loginState: LoginUiState,
    onLoginClick: (String, String) -> Unit,
    onLoginSuccess: () -> Unit,
    onNavigateToRegistration: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val isLoginStateLoading = loginState is LoginUiState.Loading

    ReportDrawnWhen { !isLoginStateLoading }

    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        LoginContent(
            loginState = loginState,
            onLoginClick = onLoginClick,
            onLoginSuccess = onLoginSuccess,
            onNavigateToRegistration = onNavigateToRegistration,
        )
    }

    AnimatedVisibility(
        visible = isLoginStateLoading,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> -fullHeight },
        ) + fadeIn(),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> -fullHeight },
        ) + fadeOut(),
    ) {
        val loadingContentDescriptor =
            stringResource(id = R.string.feature_auth_loading_login_content_description)

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

@DevicePreviews
@Composable
fun LoginScreenPreview() {
    PmTheme {
        LoginScreen(
            loginState = LoginUiState.Idle,
            onLoginClick = { _, _ -> },
            onLoginSuccess = {},
            onNavigateToRegistration = {}
        )
    }
}


