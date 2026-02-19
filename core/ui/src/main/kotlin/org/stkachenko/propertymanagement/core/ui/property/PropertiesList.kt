package org.stkachenko.propertymanagement.core.ui.property

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import org.stkachenko.propertymanagement.core.designsystem.theme.PmTheme
import org.stkachenko.propertymanagement.core.model.data.property.Property

fun LazyStaggeredGridScope.propertiesList(
    propertiesState: PropertiesUiState,
    onPropertyClick: (String) -> Unit,
) {
    when (propertiesState) {
        PropertiesUiState.Loading -> Unit
        is PropertiesUiState.Success -> {
            items(
                items = propertiesState.properties,
                key = { it.id },
                contentType = { "propertyItem" },
            ) { property ->
                PropertyCardExpanded(
                    property = property,
                    onPropertyClick = onPropertyClick,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                )
            }
        }
    }
}

sealed interface PropertiesUiState {

    data object Loading : PropertiesUiState

    data class Success(val properties: List<Property>) : PropertiesUiState
}

@Preview
@Composable
private fun PropertiesListLoadingPreview() {
    PmTheme {
        LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Adaptive(300.dp)) {
            propertiesList(
                propertiesState = PropertiesUiState.Loading,
                onPropertyClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun PropertiesListContentPreview(
    @PreviewParameter(UserPropertyPreviewParameterProvider::class)
    propertiesList: List<Property>,
) {
    PmTheme {
        LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Adaptive(300.dp)) {
            propertiesList(
                propertiesState = PropertiesUiState.Success(propertiesList),
                onPropertyClick = {},
            )
        }
    }
}