package org.stkachenko.propertymanagement.feature.properties

import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.activity.compose.ReportDrawnWhen
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus.Denied
import com.google.accompanist.permissions.rememberPermissionState
import org.stkachenko.propertymanagement.core.designsystem.component.PmOverlayBuildingHouseLoading
import org.stkachenko.propertymanagement.core.designsystem.component.scrollbar.DraggableScrollbar
import org.stkachenko.propertymanagement.core.designsystem.component.scrollbar.rememberDraggableScroller
import org.stkachenko.propertymanagement.core.designsystem.component.scrollbar.scrollbarState
import org.stkachenko.propertymanagement.core.designsystem.theme.PmTheme
import org.stkachenko.propertymanagement.core.model.data.property.Property
import org.stkachenko.propertymanagement.core.model.data.user.UserRole
import org.stkachenko.propertymanagement.core.ui.DevicePreviews
import org.stkachenko.propertymanagement.core.ui.property.PropertiesUiState
import org.stkachenko.propertymanagement.core.ui.property.UserPropertyPreviewParameterProvider
import org.stkachenko.propertymanagement.core.ui.property.UserRoleState
import org.stkachenko.propertymanagement.core.ui.property.propertiesList

@Composable
internal fun PropertiesRoute(
    onPropertyClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PropertiesViewModel = hiltViewModel(),
) {
    val propertiesState by viewModel.propertiesState.collectAsStateWithLifecycle()
    val userRole by viewModel.userRole.collectAsStateWithLifecycle()

    PropertiesScreen(
        propertiesState = propertiesState,
        onPropertyClick = onPropertyClick,
        userRole = userRole,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PropertiesScreen(
    propertiesState: PropertiesUiState,
    onPropertyClick: (String) -> Unit,
    userRole: UserRoleState,
    modifier: Modifier = Modifier,
) {
    val isPropertiesLoading = propertiesState is PropertiesUiState.Loading

    ReportDrawnWhen { !isPropertiesLoading }

    val itemsAvailable = propertyItemsSize(propertiesState)

    val state = rememberLazyStaggeredGridState()
    val scrollbarState = state.scrollbarState(
        itemsAvailable = itemsAvailable,
    )

    val sheetState = rememberModalBottomSheetState()
    val showSheet = remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(1),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalItemSpacing = 12.dp,
            modifier = Modifier
                .testTag("home:home_screen"),
            state = state,
        ) {
            propertiesList(
                propertiesState = propertiesState,
                onPropertyClick = onPropertyClick,
            )
        }

        AnimatedVisibility(
            visible = isPropertiesLoading,
            enter = slideInVertically(
                initialOffsetY = { fullHeight -> -fullHeight },
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { fullHeight -> -fullHeight },
            ) + fadeOut(),
        ) {
            val loadingContentDescriptor =
                stringResource(id = R.string.feature_properties_loading_content_description)
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

        state.DraggableScrollbar(
            modifier = Modifier
                .fillMaxHeight()
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(horizontal = 2.dp)
                .align(Alignment.Center),
            state = scrollbarState,
            orientation = Orientation.Vertical,
            onThumbMoved = state.rememberDraggableScroller(
                itemsAvailable = itemsAvailable,
            )
        )

        // FloatingActionButton for adding property
        FloatingActionButton(
            onClick = { showSheet.value = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ) {
            Icon(Icons.Filled.Add, contentDescription = stringResource(id = R.string.feature_properties_add_property))
        }

        // ModalBottomSheet for property addition
        if (showSheet.value) {
            ModalBottomSheet(
                onDismissRequest = { showSheet.value = false },
                sheetState = sheetState,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.feature_properties_add_property_title),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Button(onClick = { showSheet.value = false }) {
                        Text(text = stringResource(id = R.string.feature_properties_add_property_close))
                    }
                }
            }
        }
    }

    NotificationPermissionEffect()
}

fun propertyItemsSize(propertyListState: PropertiesUiState): Int =
    when (propertyListState) {
        PropertiesUiState.Loading -> 0
        is PropertiesUiState.Success -> propertyListState.properties.size
    }

@Composable
@OptIn(ExperimentalPermissionsApi::class)
private fun NotificationPermissionEffect() {
    if (LocalInspectionMode.current) return
    if (VERSION.SDK_INT < VERSION_CODES.TIRAMISU) return
    val notificationsPermissionState = rememberPermissionState(
        android.Manifest.permission.POST_NOTIFICATIONS,
    )
    LaunchedEffect(notificationsPermissionState) {
        val status = notificationsPermissionState.status
        if (status is Denied && !status.shouldShowRationale) {
            notificationsPermissionState.launchPermissionRequest()
        }
    }
}

@DevicePreviews
@Composable
fun PropertiesScreenPreview(
    @PreviewParameter(UserPropertyPreviewParameterProvider::class)
    propertiesList: List<Property>,
) {
    PmTheme {
        PropertiesScreen(
            propertiesState = PropertiesUiState.Success(propertiesList),
            onPropertyClick = {},
            userRole = UserRoleState.Success(UserRole.OWNER),
        )
    }
}
