package org.stkachenko.propertymanagement.feature.properties

import androidx.activity.compose.ReportDrawnWhen
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.stkachenko.propertymanagement.core.designsystem.component.PmOverlayBuildingHouseLoading
import org.stkachenko.propertymanagement.core.designsystem.theme.PmTheme
import org.stkachenko.propertymanagement.core.model.data.property.Property
import org.stkachenko.propertymanagement.core.model.data.user.UserRole
import org.stkachenko.propertymanagement.core.ui.DevicePreviews
import org.stkachenko.propertymanagement.core.ui.property.PropertyContent
import org.stkachenko.propertymanagement.core.ui.property.PropertyUiState
import org.stkachenko.propertymanagement.core.ui.property.UserPropertyPreviewParameterProvider
import org.stkachenko.propertymanagement.core.ui.property.UserRoleState

@Composable
fun PropertyDetailsRoute(
    propertyId: String,
    modifier: Modifier = Modifier,
    viewModel: PropertyDetailsViewModel = hiltViewModel(),
) {
    // propertyId picked up via SavedStateHandle in ViewModel
    val propertyState by viewModel.propertyState.collectAsStateWithLifecycle()
    val userRole by viewModel.userRole.collectAsStateWithLifecycle()

    PropertyDetailsScreen(
        propertyState = propertyState,
        userRoleState = userRole,
        modifier = modifier,
    )
}

@Composable
fun PropertyDetailsScreen(
    propertyState: PropertyUiState,
    userRoleState: UserRoleState,
    modifier: Modifier = Modifier,
) {
    val isPropertyLoading = propertyState is PropertyUiState.Loading

    ReportDrawnWhen { !isPropertyLoading }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        PropertyContent(
            propertyState = propertyState,
            userRole = userRoleState,
        )

        AnimatedVisibility(
            visible = isPropertyLoading,
            enter = slideInVertically(
                initialOffsetY = { fullHeight -> -fullHeight },
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { fullHeight -> -fullHeight },
            ) + fadeOut(),
//            modifier = Modifier.align(Alignment.TopCenter)
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
    }
}

@DevicePreviews
@Composable
private fun PropertyDetailsOwnerPreview(
    @PreviewParameter(UserPropertyPreviewParameterProvider::class)
    propertiesList: List<Property>,
) {
    PmTheme {
        PropertyDetailsScreen(
            propertyState = PropertyUiState.Success(propertiesList.first()),
            userRoleState = UserRoleState.Success(UserRole.OWNER),
        )
    }
}

@DevicePreviews
@Composable
private fun PropertyDetailsTenantPreview(
    @PreviewParameter(UserPropertyPreviewParameterProvider::class)
    propertiesList: List<Property>,
) {
    PmTheme {
        PropertyDetailsScreen(
            propertyState = PropertyUiState.Success(propertiesList.first()),
            userRoleState = UserRoleState.Success(UserRole.TENANT),
        )
    }
}
