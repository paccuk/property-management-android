package org.stkachenko.propertymanagement.feature.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.stkachenko.propertymanagement.core.designsystem.theme.PmTheme
import org.stkachenko.propertymanagement.core.ui.DevicePreviews
import org.stkachenko.propertymanagement.core.ui.auth.CompleteRegistrationContent
import org.stkachenko.propertymanagement.core.ui.auth.CompleteRegistrationUiState

@Composable
internal fun CompleteRegistrationRoute(
    onCompleteRegistrationSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CompleteRegistrationViewModel = hiltViewModel(),
) {
    val completeRegistrationState by viewModel.completeRegistrationState.collectAsStateWithLifecycle()

    CompleteRegistrationScreen(
        onCompleteRegistrationClick = { firstName, lastName, phoneNumber, role, avatarImageUrl ->
            viewModel.completeRegistration(firstName, lastName, phoneNumber, role, avatarImageUrl)
        },
        completeRegistrationState = completeRegistrationState,
        onCompleteRegistrationSuccess = onCompleteRegistrationSuccess,
        modifier = modifier,
    )
}

@Composable
fun CompleteRegistrationScreen(
    completeRegistrationState: CompleteRegistrationUiState,
    onCompleteRegistrationClick: (String, String, String, String, String?) -> Unit,
    onCompleteRegistrationSuccess: () -> Unit,
    modifier: Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        CompleteRegistrationContent(
            completeRegistrationState = completeRegistrationState,
            onCompleteRegistrationClick = onCompleteRegistrationClick,
            onCompleteRegistrationSuccess = onCompleteRegistrationSuccess,
        )
    }
}

@DevicePreviews
@Composable
fun CompleteRegistrationScreenPreview() {
    PmTheme {
        CompleteRegistrationScreen(
            completeRegistrationState = CompleteRegistrationUiState.Idle,
            onCompleteRegistrationClick = { _, _, _, _, _ -> },
            onCompleteRegistrationSuccess = {},
            modifier = Modifier,
        )
    }
}