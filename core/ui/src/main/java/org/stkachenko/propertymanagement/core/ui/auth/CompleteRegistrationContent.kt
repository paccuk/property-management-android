package org.stkachenko.propertymanagement.core.ui.auth

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ImageSearch
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.stkachenko.propertymanagement.core.designsystem.theme.PmTheme
import org.stkachenko.propertymanagement.core.ui.R

@Composable
fun CompleteRegistrationContent(
    completeRegistrationState: CompleteRegistrationUiState,
    onCompleteRegistrationClick: (String, String, String, String, String?) -> Unit,
    onCompleteRegistrationSuccess: () -> Unit,
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var avatarImageUri by remember { mutableStateOf<Uri?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ActionTitle(R.string.core_ui_auth_complete_registration_title)
        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            AvatarImageInput(
                avatarImageUri = avatarImageUri,
                onAvatarImageUrlChange = { avatarImageUri = it }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        InputTextField(
            value = firstName,
            onValueChange = { firstName = it },
            stringResId = R.string.core_ui_auth_first_name_text_field
        )
        Spacer(modifier = Modifier.height(8.dp))

        InputTextField(
            value = lastName,
            onValueChange = { lastName = it },
            stringResId = R.string.core_ui_auth_last_name_text_field
        )
        Spacer(modifier = Modifier.height(8.dp))

        InputTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            stringResId = R.string.core_ui_auth_phone_text_field
        )
        Spacer(modifier = Modifier.height(8.dp))

        RoleSelector(
            selectedRole = role,
            onRoleSelected = { role = it },
            stringResId = R.string.core_ui_auth_role_text_field
        )
        Spacer(modifier = Modifier.height(16.dp))

        StateDependentContent(
            completeRegistrationState = completeRegistrationState,
            onCompleteRegistrationClick = onCompleteRegistrationClick,
            onCompleteRegistrationSuccess = onCompleteRegistrationSuccess,
            firstName = firstName,
            lastName = lastName,
            phoneNumber = phoneNumber,
            role = role,
            avatarImageUri = avatarImageUri,
        )
    }
}

@Composable
private fun StateDependentContent(
    completeRegistrationState: CompleteRegistrationUiState,
    onCompleteRegistrationClick: (String, String, String, String, String?) -> Unit,
    onCompleteRegistrationSuccess: () -> Unit,
    firstName: String,
    lastName: String,
    phoneNumber: String,
    role: String,
    avatarImageUri: Uri?,
) {
    AuthButton(
        onClick = {
            onCompleteRegistrationClick(
                firstName,
                lastName,
                phoneNumber,
                role,
                avatarImageUri?.toString()
            )
        },
        stringResId = R.string.core_ui_auth_complete_registration_button,
        modifier = Modifier.fillMaxWidth()
    )

    when (completeRegistrationState) {
        CompleteRegistrationUiState.Idle -> Unit
        CompleteRegistrationUiState.Success -> onCompleteRegistrationSuccess
        is CompleteRegistrationUiState.Error -> {
            Text(text = completeRegistrationState.message, color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
fun AvatarImageInput(
    avatarImageUri: Uri?,
    onAvatarImageUrlChange: (Uri?) -> Unit,
) {
    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            onAvatarImageUrlChange(uri)
        }

    Box(
        modifier = Modifier
            .size(128.dp)
            .clip(CircleShape)
            .clickable {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (avatarImageUri == null) {
            IconButton(
                onClick = { pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                modifier = Modifier.size(128.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.ImageSearch,
                    contentDescription = "Add photo",
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        } else {
            AsyncImage(
                model = avatarImageUri,
                contentDescription = "Avatar",
                modifier = Modifier.size(128.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}

sealed interface CompleteRegistrationUiState {
    data object Idle : CompleteRegistrationUiState
    data object Success : CompleteRegistrationUiState
    data class Error(val message: String) : CompleteRegistrationUiState
}

@Preview
@Composable
private fun CompleteRegistrationContentIdlePreview() {
    CompositionLocalProvider(
        LocalInspectionMode provides true
    ) {
        PmTheme {
            Surface {
                CompleteRegistrationContent(
                    completeRegistrationState = CompleteRegistrationUiState.Idle,
                    onCompleteRegistrationClick = { _, _, _, _, _ -> },
                    onCompleteRegistrationSuccess = { }
                )
            }
        }
    }
}

@Preview
@Composable
private fun CompleteRegistrationContentErrorPreview() {
    CompositionLocalProvider(
        LocalInspectionMode provides true
    ) {
        PmTheme {
            Surface {
                CompleteRegistrationContent(
                    completeRegistrationState = CompleteRegistrationUiState.Error("Sample error message"),
                    onCompleteRegistrationClick = { _, _, _, _, _ -> },
                    onCompleteRegistrationSuccess = { }
                )
            }
        }
    }
}