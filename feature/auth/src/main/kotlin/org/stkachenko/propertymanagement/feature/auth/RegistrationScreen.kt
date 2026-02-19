package org.stkachenko.propertymanagement.feature.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.stkachenko.propertymanagement.core.designsystem.theme.PmTheme
import org.stkachenko.propertymanagement.core.ui.DevicePreviews
import org.stkachenko.propertymanagement.core.ui.auth.RegistrationContent
import org.stkachenko.propertymanagement.core.ui.auth.RegistrationUiState

@Composable
internal fun RegistrationRoute(
    onNavigateToCompleteRegistration: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegistrationViewModel = hiltViewModel(),
) {
    val registrationState by viewModel.registrationState.collectAsStateWithLifecycle()

    LaunchedEffect(registrationState) {
        if (registrationState is RegistrationUiState.TokenReceived) {
            onNavigateToCompleteRegistration()
        }
    }

    RegistrationScreen(
        onRegisterClick = { email, password, confirmPassword ->
            viewModel.register(email, password, confirmPassword)
        },
        registrationState = registrationState,
        onNavigateToLogin = onNavigateToLogin,
        modifier = modifier,
    )
}

@Composable
fun RegistrationScreen(
    registrationState: RegistrationUiState,
    onRegisterClick: (String, String, String) -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        RegistrationContent(
            registrationState = registrationState,
            onRegisterClick = onRegisterClick,
            onNavigateToLogin = onNavigateToLogin,
        )
    }
}

@DevicePreviews
@Composable
fun RegistrationScreenPreview() {
    PmTheme {
        RegistrationScreen(
            registrationState = RegistrationUiState.Idle,
            onRegisterClick = { _, _, _ -> },
            onNavigateToLogin = {},
        )
    }
}