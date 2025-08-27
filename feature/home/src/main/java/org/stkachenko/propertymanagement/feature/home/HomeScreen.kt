package org.stkachenko.propertymanagement.feature.home

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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import org.stkachenko.propertymanagement.core.designsystem.component.PmOverlayLoadingWheel
import org.stkachenko.propertymanagement.core.designsystem.component.scrollbar.DraggableScrollbar
import org.stkachenko.propertymanagement.core.designsystem.component.scrollbar.rememberDraggableScroller
import org.stkachenko.propertymanagement.core.designsystem.component.scrollbar.scrollbarState
import org.stkachenko.propertymanagement.core.designsystem.theme.PmTheme
import org.stkachenko.propertymanagement.core.model.data.property.Property
import org.stkachenko.propertymanagement.core.ui.DevicePreviews
import org.stkachenko.propertymanagement.core.ui.PropertyListUiState
import org.stkachenko.propertymanagement.core.ui.UserPropertyPreviewParameterProvider
import org.stkachenko.propertymanagement.core.ui.propertiesList

@Composable
internal fun HomeRoute(
    onPropertyClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val propertiesState by viewModel.propertiesUiState.collectAsStateWithLifecycle()
    val isSyncing by viewModel.isSyncing.collectAsStateWithLifecycle()

    HomeScreen(
        isSyncing = isSyncing,
        propertyListState = propertiesState,
        onPropertyClick = onPropertyClick,
        modifier = modifier,
    )
}

@Composable
internal fun HomeScreen(
    isSyncing: Boolean,
    propertyListState: PropertyListUiState,
    onPropertyClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isPropertiesLoading = propertyListState is PropertyListUiState.Loading

    ReportDrawnWhen { !isSyncing && !isPropertiesLoading }

    val itemsAvailable = propertyItemsSize(propertyListState)

    val state = rememberLazyStaggeredGridState()
    val scrollbarState = state.scrollbarState(
        itemsAvailable = itemsAvailable,
    )

    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        Column {
            LazyHorizontalStaggeredGrid(
                rows = StaggeredGridCells.Fixed(1),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalItemSpacing = 24.dp,
                modifier = Modifier
                    .testTag("home:properties_list"),
                state = state,
            ) {
                propertiesList(
                    propertyListState = propertyListState,
                    onPropertyClick = onPropertyClick,
                )
            }
        }

        AnimatedVisibility(
            visible = isSyncing || isPropertiesLoading,
            enter = slideInVertically(
                initialOffsetY = { fullHeight -> -fullHeight },
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { fullHeight -> -fullHeight },
            ) + fadeOut(),
        ) {
            val loadingContentDescriptor =
                stringResource(id = R.string.feature_home_loading_content_description)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            ) {
                PmOverlayLoadingWheel(
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
    }

    NotificationPermissionEffect()

}

private fun propertyItemsSize(propertiesState: PropertyListUiState): Int =
    when (propertiesState) {
        is PropertyListUiState.Loading -> 0
        is PropertyListUiState.Success -> propertiesState.properties.size

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
fun HomeScreenPreview(
    @PreviewParameter(UserPropertyPreviewParameterProvider::class)
    propertiesList: List<Property>,
) {
    PmTheme {
        HomeScreen(
            isSyncing = false,
            propertyListState = PropertyListUiState.Success(propertiesList),
            onPropertyClick = {},
        )
    }
}

