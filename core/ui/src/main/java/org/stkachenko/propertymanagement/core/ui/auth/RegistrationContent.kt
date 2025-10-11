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
import org.stkachenko.propertymanagement.core.ui.R

@Composable
fun RegistrationContent(
    registrationState: RegistrationUiState,
    onRegisterClick: (String, String, String) -> Unit,
    onNavigateToLogin: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column {
            ActionTitle(R.string.core_ui_auth_registration_title)
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
            Spacer(modifier = Modifier.height(8.dp))

            PasswordInputTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                stringResId = R.string.core_ui_auth_confirm_password_text_field,
            )
            Spacer(modifier = Modifier.height(16.dp))

            StateDependentContent(
                registrationState = registrationState,
                onRegisterClick = onRegisterClick,
                email = email,
                password = password,
                confirmPassword = confirmPassword,
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AuthQuestionText(R.string.core_ui_auth_already_have_account_text)
                AuthQuestionTextButton(
                    onNavigateToAuthAction = onNavigateToLogin,
                    stringResId = R.string.core_ui_auth_login_text_button,
                )
            }
        }
    }
}

@Composable
private fun StateDependentContent(
    registrationState: RegistrationUiState,
    onRegisterClick: (String, String, String) -> Unit,
    email: String,
    password: String,
    confirmPassword: String,
) {
    AuthButton(
        onClick = { onRegisterClick(email, password, confirmPassword) },
        stringResId = R.string.core_ui_auth_register_button,
        modifier = Modifier.fillMaxWidth()
    )

    if (registrationState is RegistrationUiState.Error) {
        Text(text = registrationState.message, color = MaterialTheme.colorScheme.error)
    }
}

sealed interface RegistrationUiState {
    data object Idle : RegistrationUiState
    data object TokenReceived : RegistrationUiState
    data class Error(val message: String) : RegistrationUiState
}

@Preview
@Composable
private fun RegistrationContentIdlePreview() {
    CompositionLocalProvider(
        LocalInspectionMode provides true
    ) {
        PmTheme {
            Surface {
                RegistrationContent(
                    registrationState = RegistrationUiState.Idle,
                    onRegisterClick = { _, _, _ -> },
                    onNavigateToLogin = {},
                )
            }
        }
    }
}

@Preview
@Composable
private fun RegistrationContentErrorPreview() {
    CompositionLocalProvider(
        LocalInspectionMode provides true
    ) {
        PmTheme {
            Surface {
                RegistrationContent(
                    registrationState = RegistrationUiState.Error("Sample error message"),
                    onRegisterClick = { _, _, _ -> },
                    onNavigateToLogin = {},
                )
            }
        }
    }
}