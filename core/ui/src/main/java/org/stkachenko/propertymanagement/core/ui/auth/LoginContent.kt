package org.stkachenko.propertymanagement.core.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.stkachenko.propertymanagement.core.designsystem.theme.PmTheme
import org.stkachenko.propertymanagement.core.ui.ActionTitle
import org.stkachenko.propertymanagement.core.ui.AuthButton
import org.stkachenko.propertymanagement.core.ui.AuthQuestionText
import org.stkachenko.propertymanagement.core.ui.AuthQuestionTextButton
import org.stkachenko.propertymanagement.core.ui.InputTextField
import org.stkachenko.propertymanagement.core.ui.PasswordInputTextField
import org.stkachenko.propertymanagement.core.ui.R

@Composable
fun LoginContent(
    loginState: LoginUiState,
    onLoginClick: (String, String) -> Unit,
    onLoginSuccess: () -> Unit,
    onNavigateToRegistration: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Column {
            ActionTitle(R.string.core_ui_auth_login_title)
            Spacer(modifier = Modifier.height(32.dp))

            InputTextField(
                value = email,
                onValueChange = { email = it },
                stringResId = R.string.core_ui_auth_email_text_field
            )
            Spacer(modifier = Modifier.height(8.dp))

            PasswordInputTextField(
                value = password,
                onValueChange = { password = it },
                stringResId = R.string.core_ui_auth_password_text_field,
            )
            Spacer(modifier = Modifier.height(16.dp))

            StateDependentContent(loginState, onLoginSuccess, onLoginClick, email, password)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AuthQuestionText(R.string.core_ui_auth_dont_have_account_text)
                AuthQuestionTextButton(
                    onNavigateToAuthAction = onNavigateToRegistration,
                    stringResId = R.string.core_ui_auth_register_text_button,
                )
            }
        }
    }
}

@Composable
private fun StateDependentContent(
    loginState: LoginUiState,
    onLoginSuccess: () -> Unit,
    onLoginClick: (String, String) -> Unit,
    email: String,
    password: String,
) {
    when (loginState) {
        LoginUiState.Success -> onLoginSuccess
        LoginUiState.Loading -> Unit
        LoginUiState.Idle -> {
            AuthButton(
                onClick = { onLoginClick(email, password) },
                stringResId = R.string.core_ui_auth_login_button,
                modifier = Modifier.fillMaxWidth()
            )
        }

        is LoginUiState.Error -> {
            Text(text = loginState.message, color = MaterialTheme.colorScheme.error)
        }
    }
}

sealed interface LoginUiState {
    data object Idle : LoginUiState
    data object Loading : LoginUiState
    data object Success : LoginUiState
    data class Error(val message: String) : LoginUiState
}

@Preview
@Composable
private fun LoginContentIdlePreview() {
    CompositionLocalProvider(
        LocalInspectionMode provides true
    ) {
        PmTheme {
            Surface {
                LoginContent(
                    loginState = LoginUiState.Idle,
                    onLoginClick = { _, _ -> },
                    onLoginSuccess = {},
                    onNavigateToRegistration = {}
                )
            }
        }
    }
}

@Preview
@Composable
private fun LoginContentLoadingPreview() {
    CompositionLocalProvider(
        LocalInspectionMode provides true
    ) {
        PmTheme {
            Surface {
                LoginContent(
                    loginState = LoginUiState.Loading,
                    onLoginClick = { _, _ -> },
                    onLoginSuccess = {},
                    onNavigateToRegistration = {}
                )
            }
        }
    }
}