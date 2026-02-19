package org.stkachenko.propertymanagement.core.ui.profile

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import org.stkachenko.propertymanagement.core.designsystem.R.drawable
import org.stkachenko.propertymanagement.core.designsystem.component.PmOverlayBuildingHouseLoading
import org.stkachenko.propertymanagement.core.designsystem.theme.PmTheme
import org.stkachenko.propertymanagement.core.localization.Language
import org.stkachenko.propertymanagement.core.model.data.user.User
import org.stkachenko.propertymanagement.core.model.data.userdata.DarkThemeConfig
import org.stkachenko.propertymanagement.core.ui.InputTextField
import org.stkachenko.propertymanagement.core.ui.PasswordInputTextField
import org.stkachenko.propertymanagement.core.ui.R


@Composable
fun ProfileContent(
    profileState: ProfileUiState,
    settingsUiState: SettingsUiState,
    localeUiState: LocaleUiState,
    onEditProfileClick: (String, String, String, String) -> Unit,
    onChangeDarkThemeConfig: (darkThemeConfig: DarkThemeConfig) -> Unit,
    onLanguageChange: (String) -> Unit,
    onChangePassword: (String, String, String) -> Unit,
    onLogout: () -> Unit,
) {
    when (profileState) {
        is ProfileUiState.Loading -> Unit
        is ProfileUiState.Error -> {
            Text(text = profileState.message)
        }

        is ProfileUiState.Success -> {
            ProfileContentSuccess(
                user = profileState.user,
                settingsUiState = settingsUiState,
                localeUiState = localeUiState,
                onEditProfileClick = onEditProfileClick,
                onChangeDarkThemeConfig = onChangeDarkThemeConfig,
                onLanguageChange = onLanguageChange,
                onChangePassword = onChangePassword,
                onLogout = onLogout,
            )
        }
    }
}

@Composable
fun ProfileContentSuccess(
    user: User,
    settingsUiState: SettingsUiState,
    localeUiState: LocaleUiState,
    onEditProfileClick: (String, String, String, String) -> Unit,
    onChangeDarkThemeConfig: (darkThemeConfig: DarkThemeConfig) -> Unit,
    onLanguageChange: (String) -> Unit,
    onChangePassword: (String, String, String) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val loadingContentDescriptor = "Loading user settings"

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Column {
            HeaderSection(user)
            Spacer(modifier = Modifier.height(32.dp))
            AccountDetailsSection(user)
        }

        when (settingsUiState) {
            SettingsUiState.Loading -> PmOverlayBuildingHouseLoading(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                contentDesc = loadingContentDescriptor,
            )

            is SettingsUiState.Success -> {
                var showThemeDialog by remember { mutableStateOf(false) }
                var showEditProfileDialog by remember { mutableStateOf(false) }
                var showChangeLanguageDialog by remember { mutableStateOf(false) }
                var showChangePasswordDialog by remember { mutableStateOf(false) }

                if (showThemeDialog) {
                    ChangeThemeDialog(
                        currentDarkThemeConfig = settingsUiState.settings.darkThemeConfig,
                        onDismissRequest = { showThemeDialog = false },
                        onChangeDarkThemeConfig = onChangeDarkThemeConfig
                    )
                }

                if (showEditProfileDialog) {
                    EditProfileDialog(
                        user = user,
                        onDismissRequest = { showEditProfileDialog = false },
                        onSaveClick = onEditProfileClick,
                    )
                }

                if (showChangeLanguageDialog) {
                    ChangeLanguageDialog(
                        selectedLanguage = localeUiState.selectedLanguage,
                        onDismissRequest = { showChangeLanguageDialog = false },
                        onChangeLanguage = onLanguageChange
                    )
                }

                if (showChangePasswordDialog) {
                    ChangePasswordDialog(
                        onDismissRequest = { showChangePasswordDialog = false },
                        onChangePassword = onChangePassword
                    )
                }

                SettingsSection(
                    onEditProfileClick = { showEditProfileDialog = true },
                    onThemeChangeClick = { showThemeDialog = true },
                    onLanguageChangeClick = { showChangeLanguageDialog = true },
                    onChangePasswordClick = { showChangePasswordDialog = true },
                    onLogoutClick = onLogout
                )
            }
        }
    }
}

@Composable
private fun HeaderSection(user: User) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth(),
    ) {
        ProfileAvatar(
            avatarImageUrl = user.avatarImageUrl
        )
    }
    Spacer(modifier = Modifier.height(16.dp))

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = "${user.firstName} ${user.lastName}",
            style = MaterialTheme.typography.headlineMedium,
        )
    }
}

@Composable
private fun AccountDetailsSection(user: User) {
    Column {
        Text(
            text = stringResource(R.string.core_ui_profile_account_details_label),
            style = MaterialTheme.typography.labelMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                AccountDetailItem(
                    icon = Icons.Outlined.Email,
                    label = "Email",
                    value = user.email
                )
                HorizontalDivider()
                AccountDetailItem(
                    icon = Icons.Outlined.Phone,
                    label = "Phone",
                    value = user.phone
                )
            }
        }
    }
}

@Composable
private fun AccountDetailItem(
    icon: ImageVector,
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
        )
    }
}

@Composable
private fun SettingsSection(
    onEditProfileClick: () -> Unit,
    onThemeChangeClick: () -> Unit,
    onLanguageChangeClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onLogoutClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.core_ui_profile_settings_label),
            style = MaterialTheme.typography.labelMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                SettingItem(
                    icon = Icons.Outlined.Edit,
                    text = stringResource(R.string.core_ui_profile_settings_edit_profile_item_text),
                    onClick = onEditProfileClick
                )
                HorizontalDivider()
                SettingItem(
                    icon = Icons.Outlined.Palette,
                    text = stringResource(R.string.core_ui_profile_settings_theme_item_text),
                    onClick = onThemeChangeClick
                )
                HorizontalDivider()
                SettingItem(
                    icon = Icons.Outlined.Language,
                    text = stringResource(R.string.core_ui_profile_settings_language_item_text),
                    onClick = onLanguageChangeClick
                )
                HorizontalDivider()
                SettingItem(
                    icon = Icons.Outlined.Password,
                    text = stringResource(R.string.core_ui_profile_settings_change_password_item_text),
                    onClick = onChangePasswordClick
                )
                HorizontalDivider()
                SettingItem(
                    icon = Icons.AutoMirrored.Outlined.Logout,
                    text = stringResource(R.string.core_ui_profile_settings_logout_item_text),
                    onClick = onLogoutClick,
                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7F)
                )

            }
        }
    }
}

@Composable
private fun SettingItem(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    tint: Color = LocalContentColor.current,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = tint
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = tint,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = tint.copy(alpha = 0.6f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordDialog(
    onDismissRequest: () -> Unit,
    onChangePassword: (String, String, String) -> Unit,
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.surface,
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = stringResource(R.string.core_ui_profile_settings_change_password_item_text),
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 22.sp,
                                lineHeight = 2.sp,
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = onDismissRequest) {
                                Icon(
                                    Icons.Default.Close,
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    contentDescription = null
                                )
                            }
                        },
                        actions = {
                            TextButton(
                                onClick = {
                                    onChangePassword(
                                        currentPassword,
                                        newPassword,
                                        confirmNewPassword
                                    )
                                    onDismissRequest()
                                },
                                enabled = currentPassword.isNotBlank() &&
                                        newPassword.isNotBlank() &&
                                        confirmNewPassword.isNotBlank()
                            ) {
                                Text(stringResource(R.string.core_ui_profile_edit_dialog_save_text_button))
                            }
                        }
                    )
                },
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Top,
                ) {
                    PasswordInputTextField(
                        value = currentPassword,
                        onValueChange = { currentPassword = it },
                        stringResId = R.string.core_ui_profile_change_password_current_password_label,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    PasswordInputTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        stringResId = R.string.core_ui_profile_change_password_new_password_label,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    PasswordInputTextField(
                        value = confirmNewPassword,
                        onValueChange = { confirmNewPassword = it },
                        stringResId = R.string.core_ui_profile_change_password_confirm_new_password_label,
                    )
                }
            }
        }
    }
}

@Composable
fun ChangeLanguageDialog(
    selectedLanguage: String,
    onDismissRequest: () -> Unit,
    onChangeLanguage: (String) -> Unit,
) {
    val radioOptions = mapOf(
        Language.ENGLISH.code to Language.ENGLISH.displayLanguage,
        Language.POLISH.code to Language.POLISH.displayLanguage,
    )
    var selectedLang by remember { mutableStateOf(selectedLanguage) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(stringResource(R.string.core_ui_profile_settings_select_language_dialog_title)) },
        text = {
            Column(Modifier.selectableGroup()) {
                radioOptions.forEach { (langCode, label) ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .selectable(
                                selected = (langCode == selectedLang),
                                onClick = { selectedLang = langCode },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (langCode == selectedLang),
                            onClick = null
                        )
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onChangeLanguage(selectedLang)
                    onDismissRequest()
                }
            ) {
                Text(stringResource(R.string.core_ui_profile_settings_ok_button))
            }
        }
    )
}

@Composable
fun ChangeThemeDialog(
    currentDarkThemeConfig: DarkThemeConfig,
    onDismissRequest: () -> Unit,
    onChangeDarkThemeConfig: (darkThemeConfig: DarkThemeConfig) -> Unit,
) {
    val radioOptions = mapOf(
        DarkThemeConfig.FOLLOW_SYSTEM to stringResource(R.string.core_ui_profile_settings_follow_system_radio_option),
        DarkThemeConfig.LIGHT to stringResource(R.string.core_ui_profile_settings_light_radio_option),
        DarkThemeConfig.DARK to stringResource(R.string.core_ui_profile_settings_dark_radio_option)
    )
    var selectedConfig by remember { mutableStateOf(currentDarkThemeConfig) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(stringResource(R.string.core_ui_profile_settings_select_theme_text)) },
        text = {
            Column(Modifier.selectableGroup()) {
                radioOptions.forEach { (config, label) ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .selectable(
                                selected = (config == selectedConfig),
                                onClick = { selectedConfig = config },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (config == selectedConfig),
                            onClick = null
                        )
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onChangeDarkThemeConfig(selectedConfig)
                    onDismissRequest()
                }
            ) {
                Text(stringResource(R.string.core_ui_profile_settings_ok_button))
            }
        }
    )
}

@Composable
fun ProfileAvatar(
    avatarImageUrl: String?,
) {
    val isLocalInspection = LocalInspectionMode.current
    val placeholder =
        painterResource(id = drawable.core_designsystem_ic_profile_avatar_placeholder)

    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    val painter = rememberAsyncImagePainter(
        model = avatarImageUrl,
        onState = { state ->
            isLoading = state is AsyncImagePainter.State.Loading
            isError = state is AsyncImagePainter.State.Error
        },
    )

    Box(
        modifier = Modifier
            .size(150.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(64.dp),
                color = MaterialTheme.colorScheme.tertiary,
            )
        }

        Image(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            painter = if (isError.not() && !isLocalInspection) {
                painter
            } else {
                placeholder
            },
            contentDescription = null,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileDialog(
    user: User,
    onDismissRequest: () -> Unit,
    onSaveClick: (firstName: String, lastName: String, email: String, phone: String) -> Unit,
) {
    var firstName by remember(user.firstName) { mutableStateOf(user.firstName) }
    var lastName by remember(user.lastName) { mutableStateOf(user.lastName) }
    var email by remember(user.email) { mutableStateOf(user.email) }
    var phone by remember(user.phone) { mutableStateOf(user.phone) }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.surface,
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = stringResource(R.string.core_ui_profile_edit_dialog_header),
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 22.sp,
                                lineHeight = 2.sp,
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = onDismissRequest) {
                                Icon(
                                    Icons.Default.Close,
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    contentDescription = null
                                )
                            }
                        },
                        actions = {
                            TextButton(
                                onClick = {
                                    onSaveClick(firstName, lastName, email, phone)
                                    onDismissRequest()
                                },
                            ) {
                                Text(stringResource(R.string.core_ui_profile_edit_dialog_save_text_button))
                            }
                        }
                    )
                },
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column {
                        InputTextField(
                            value = firstName,
                            onValueChange = { firstName = it },
                            stringResId = R.string.core_ui_profile_edit_dialog_first_name_label,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        InputTextField(
                            value = lastName,
                            onValueChange = { lastName = it },
                            stringResId = R.string.core_ui_profile_edit_dialog_last_name_label,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        InputTextField(
                            value = email,
                            onValueChange = { email = it },
                            stringResId = R.string.core_ui_profile_edit_dialog_email_label,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        InputTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            stringResId = R.string.core_ui_profile_edit_dialog_phone_name_label,
                        )
                    }
                }
            }
        }
    }
}

sealed interface ProfileUiState {
    data object Loading : ProfileUiState
    data class Success(val user: User) : ProfileUiState
    data class Error(val message: String) : ProfileUiState
}

data class UserEditableSettings(
    val useDynamicColor: Boolean,
    val darkThemeConfig: DarkThemeConfig,
)

sealed interface SettingsUiState {
    data object Loading : SettingsUiState
    data class Success(val settings: UserEditableSettings) : SettingsUiState
}

data class LocaleUiState(
    val selectedLanguage: String = "",
)

@Preview
@Composable
private fun ChangePasswordDialogPreview() {
    CompositionLocalProvider(
        LocalInspectionMode provides true
    ) {
        PmTheme {
            Surface {
                ChangePasswordDialog(
                    onDismissRequest = {},
                    onChangePassword = { _, _, _ -> }
                )
            }
        }
    }
}


@Preview
@Composable
private fun EditProfileDialogPreview(
    @PreviewParameter(OwnerProfilePreviewParameterProvider::class)
    usersList: List<User>,
) {
    CompositionLocalProvider(
        LocalInspectionMode provides true
    ) {
        PmTheme {
            Surface {
                EditProfileDialog(
                    user = usersList[0],
                    onDismissRequest = {},
                    onSaveClick = { _, _, _, _ -> }
                )
            }
        }
    }
}

@Preview
@Composable
private fun ChangeLanguageDialogPreview() {
    CompositionLocalProvider(
        LocalInspectionMode provides true
    ) {
        PmTheme {
            Surface {
                ChangeLanguageDialog(
                    selectedLanguage = Language.ENGLISH.code,
                    onDismissRequest = {},
                    onChangeLanguage = {}
                )
            }
        }
    }
}

@Preview
@Composable
private fun ChangeThemeDialogPreview() {
    CompositionLocalProvider(
        LocalInspectionMode provides true
    ) {
        PmTheme {
            Surface {
                ChangeThemeDialog(
                    currentDarkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
                    onDismissRequest = {},
                    onChangeDarkThemeConfig = {}
                )
            }
        }
    }
}

@Preview
@Composable
private fun OwnerProfileContentSuccessPreview(
    @PreviewParameter(OwnerProfilePreviewParameterProvider::class)
    usersList: List<User>,
) {
    CompositionLocalProvider(
        LocalInspectionMode provides true
    ) {
        PmTheme {
            Surface {
                ProfileContent(
                    profileState = ProfileUiState.Success(usersList[0]),
                    settingsUiState = SettingsUiState.Success(
                        settings = UserEditableSettings(
                            useDynamicColor = true,
                            darkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
                        )
                    ),
                    localeUiState = LocaleUiState(
                        selectedLanguage = Language.ENGLISH.code
                    ),
                    onEditProfileClick = { _, _, _, _ -> },
                    onChangeDarkThemeConfig = {},
                    onLanguageChange = {},
                    onChangePassword = { _, _, _ -> },
                    onLogout = {},
                )
            }
        }
    }
}

@Preview
@Composable
private fun TenantProfileContentSuccessPreview(
    @PreviewParameter(TenantProfilePreviewParameterProvider::class)
    usersList: List<User>,
) {
    CompositionLocalProvider(
        LocalInspectionMode provides true
    ) {
        PmTheme {
            Surface {
                ProfileContent(
                    profileState = ProfileUiState.Success(usersList[0]),
                    settingsUiState = SettingsUiState.Success(
                        settings = UserEditableSettings(
                            useDynamicColor = true,
                            darkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
                        )
                    ),
                    localeUiState = LocaleUiState(
                        selectedLanguage = Language.ENGLISH.code
                    ),
                    onEditProfileClick = { _, _, _, _ -> },
                    onChangeDarkThemeConfig = {},
                    onLanguageChange = {},
                    onChangePassword = { _, _, _ -> },
                    onLogout = {}
                )
            }
        }
    }
}

@Preview
@Composable
private fun ProfileContentLoadingPreview() {
    CompositionLocalProvider(
        LocalInspectionMode provides true
    ) {
        PmTheme {
            Surface {
                ProfileContent(
                    profileState = ProfileUiState.Loading,
                    settingsUiState = SettingsUiState.Success(
                        settings = UserEditableSettings(
                            useDynamicColor = true,
                            darkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
                        )
                    ),
                    localeUiState = LocaleUiState(
                        selectedLanguage = Language.ENGLISH.code
                    ),
                    onEditProfileClick = { _, _, _, _ -> },
                    onChangeDarkThemeConfig = {},
                    onLanguageChange = {},
                    onChangePassword = { _, _, _ -> },
                    onLogout = {}
                )
            }
        }
    }
}
