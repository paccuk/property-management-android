package org.stkachenko.propertymanagement.core.ui.auth

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.stkachenko.propertymanagement.core.model.data.user.UserRole

@Composable
internal fun ActionTitle(
    @StringRes stringResId: Int,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = stringResource(stringResId),
            style = MaterialTheme.typography.titleLarge,
        )
    }
}

@Composable
internal fun InputTextField(
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes stringResId: Int,
) {
    Column {
        Text(
            text = stringResource(stringResId),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        PmOutlinedTextField(
            value = value,
            onValueChange = onValueChange,
        )
    }
}

@Composable
internal fun PasswordInputTextField(
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes stringResId: Int,
) {
    Column {
        Text(
            text = stringResource(stringResId),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        PmOutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            visualTransformation = PasswordVisualTransformation()
        )
    }
}

@Composable
fun RoleSelector(
    selectedRole: String,
    onRoleSelected: (String) -> Unit,
    @StringRes stringResId: Int,
) {
    Column {
        Text(
            text = stringResource(stringResId),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(8.dp)
                ),
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserRole.entries.forEach { role ->
                val isSelected = selectedRole == role.role
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                        )
                        .clickable { onRoleSelected(role.role) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = role.role.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Composable
internal fun AuthButton(
    onClick: () -> Unit,
    @StringRes stringResId: Int,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(32.dp),
    ) {
        Text(
            text = stringResource(stringResId),
            style = MaterialTheme.typography.labelMedium,
        )
    }
}

@Composable
internal fun AuthQuestionText(
    @StringRes stringResId: Int,
) {
    Text(
        text = stringResource(stringResId),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface,
    )
}

@Composable
internal fun AuthQuestionTextButton(
    onNavigateToAuthAction: () -> Unit,
    @StringRes stringResId: Int,
) {
    TextButton(
        onClick = onNavigateToAuthAction,
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier.height(16.dp),
    ) {
        Text(
            text = stringResource(stringResId),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun PmOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    val interactionSource = remember { MutableInteractionSource() }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        interactionSource = interactionSource,
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .height(32.dp),
        textStyle = LocalTextStyle.current.copy(
            fontSize = 8.sp,
            color = MaterialTheme.colorScheme.onSurface,
        ),
        decorationBox = { innerTextField ->
            OutlinedTextFieldDefaults.DecorationBox(
                value = value,
                innerTextField = innerTextField,
                visualTransformation = visualTransformation,
                enabled = true,
                singleLine = true,
                interactionSource = interactionSource,
                contentPadding = PaddingValues(8.dp),
                container = {
                    OutlinedTextFieldDefaults.Container(
                        enabled = true,
                        isError = false,
                        interactionSource = interactionSource,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                        ),
                        shape = RoundedCornerShape(8.dp),
                        unfocusedBorderThickness = 1.dp,
                        focusedBorderThickness = 2.dp,
                    )
                }
            )
        }
    )
}