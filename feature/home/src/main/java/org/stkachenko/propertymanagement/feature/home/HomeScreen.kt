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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
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
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus.Denied
import com.google.accompanist.permissions.rememberPermissionState
import org.stkachenko.propertymanagement.core.designsystem.component.PmOverlayLoadingWheel
import org.stkachenko.propertymanagement.core.designsystem.component.scrollbar.DraggableScrollbar
import org.stkachenko.propertymanagement.core.designsystem.component.scrollbar.rememberDraggableScroller
import org.stkachenko.propertymanagement.core.designsystem.component.scrollbar.scrollbarState
import org.stkachenko.propertymanagement.core.designsystem.theme.PmTheme
import org.stkachenko.propertymanagement.core.model.data.property.Property
import org.stkachenko.propertymanagement.core.model.data.user.UserRole
import org.stkachenko.propertymanagement.core.ui.DevicePreviews
import org.stkachenko.propertymanagement.core.ui.PropertiesUiState
import org.stkachenko.propertymanagement.core.ui.UserPropertyPreviewParameterProvider
import org.stkachenko.propertymanagement.core.ui.propertiesList
import org.stkachenko.propertymanagement.feature.home.components.CategoryHeader
import org.stkachenko.propertymanagement.feature.home.model.HomeCategory

@Composable
internal fun HomeRoute(
    onPropertyClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavController? = null,
) {
    val propertiesState by viewModel.propertiesState.collectAsStateWithLifecycle()
    val categoriesState by viewModel.categoriesState.collectAsStateWithLifecycle()
    val isSyncing by viewModel.isSyncing.collectAsStateWithLifecycle()
    val userRole by viewModel.userRole.collectAsStateWithLifecycle()

    HomeScreen(
        isSyncing = isSyncing,
        propertiesState = propertiesState,
        categoriesUiState = categoriesState,
        onCategoryClick = { destinationRoute ->
            navController?.navigate(destinationRoute)
        },
        userRole = userRole,
        modifier = modifier,
    )
}

@Composable
internal fun HomeScreen(
    isSyncing: Boolean,
    propertiesState: PropertiesUiState,
    categoriesUiState: CategoriesUiState,
    onCategoryClick: (String) -> Unit,
    userRole: UserRole,
    modifier: Modifier = Modifier,
) {
    val isPropertiesLoading = propertiesState is PropertiesUiState.Loading

    ReportDrawnWhen { !isSyncing && !isPropertiesLoading }

    val itemsAvailable = propertyItemsSize(propertiesState)

    val state = rememberLazyStaggeredGridState()
    val scrollbarState = state.scrollbarState(
        itemsAvailable = itemsAvailable,
    )

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
            homeCategories(
                categoriesUiState = categoriesUiState,
                onCategoryClick = onCategoryClick,
            )
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

@Composable
private fun PropertiesSection(
    propertyListState: PropertiesUiState,
    onPropertyClick: () -> Unit,
    modifier: Modifier = Modifier
) {


    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(400.dp)
    ) {
        LazyRow(

        ) {
            propertiesList(
                propertyListState = propertyListState,
                onPropertyClick = onPropertyClick,
            )
        }
    }
}

private fun propertyItemsSize(propertiesState: PropertiesUiState): Int =
    when (propertiesState) {
        is PropertiesUiState.Loading -> 0
        is PropertiesUiState.Success -> propertiesState.properties.size
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

@Composable
fun CategorySection(
    category: HomeCategory,
    onSeeAllClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        CategoryHeader(
            title = stringResource(id = category.titleResId),
            onSeeAllClick = onSeeAllClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .testTag("home:category_${category.titleResId}")
        )
        category.content()
    }
}

private fun LazyStaggeredGridScope.homeCategories(
    categoriesUiState: CategoriesUiState,
    onCategoryClick: (String) -> Unit,
) {
    when (categoriesUiState) {
        CategoriesUiState.Loading -> Unit
        is CategoriesUiState.Success -> {
            items(
                items = categoriesUiState.categories,
                key = { it.titleResId },
                contentType = { "homeCategoryItem" },
            ) { category ->
                CategorySection(
                    category = category,
                    onSeeAllClick = { onCategoryClick(category.destinationRoute) },
                )
            }
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
            propertyListState = PropertiesUiState.Success(propertiesList),
            onPropertyClick = {},
            categories = listOf(
                HomeCategory(
                    titleResId = R.string.feature_home_properties_category,
                    destinationRoute = "properties_route"
                ),
                HomeCategory(
                    titleResId = R.string.feature_home_chats_category,
                    destinationRoute = "chats_route"
                )
            ),
            onCategoryClick = {},
            userRole = UserRole.OWNER
        )
    }
}
