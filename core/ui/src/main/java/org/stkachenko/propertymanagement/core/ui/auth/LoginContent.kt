package org.stkachenko.propertymanagement.core.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.stkachenko.propertymanagement.core.designsystem.theme.PmTheme
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
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        LoginHeader()
        Spacer(modifier = Modifier.height(16.dp))

        EmailInputField(
            email = email,
            onEmailChange = { email = it },
        )
        Spacer(modifier = Modifier.height(8.dp))

        PasswordInputField(
            password = password,
            onPasswordChange = { password = it },
        )
        Spacer(modifier = Modifier.height(16.dp))

        StateDependentContent(loginState, onLoginSuccess, onLoginClick, email, password)
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            NoAccountQuestionText()
            RegisterTextButton(onNavigateToRegistration)
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
            Button(
                onClick = { onLoginClick(email, password) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.core_ui_login_proceed_button))
            }
        }

        is LoginUiState.Error -> {
            Text(text = loginState.message, color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
private fun NoAccountQuestionText() {
    Text(
        text = stringResource(R.string.core_ui_login_no_account_text),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface,
    )
}

@Composable
private fun RegisterTextButton(onNavigateToRegistration: () -> Unit) {
    TextButton(
        onClick = onNavigateToRegistration,
        contentPadding = PaddingValues(horizontal = 4.dp),
    ) {
        Text(
            text = stringResource(R.string.core_ui_login_register_text_button),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
private fun EmailInputField(
    email: String,
    onEmailChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = email,
        onValueChange = onEmailChange,
        label = {
            Text(text = stringResource(R.string.core_ui_login_email_text_field))
        },
        shape = RoundedCornerShape(100),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun PasswordInputField(
    password: String,
    onPasswordChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = {
            Text(text = stringResource(R.string.core_ui_login_password_text_field))
        },
        shape = RoundedCornerShape(100),
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = PasswordVisualTransformation(),
    )
}

@Composable
private fun LoginHeader() {
    Text(
        text = stringResource(R.string.core_ui_login_title),
        style = MaterialTheme.typography.headlineMedium,
    )
}

sealed interface LoginUiState {
    object Idle : LoginUiState
    object Success : LoginUiState
    object Loading : LoginUiState
    data class Error(val message: String) : LoginUiState
}

@Preview
@Preview(device = Devices.TABLET)
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
@Preview(device = Devices.TABLET)
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