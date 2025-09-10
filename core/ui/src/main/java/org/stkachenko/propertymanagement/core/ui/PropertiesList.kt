package org.stkachenko.propertymanagement.core.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import org.stkachenko.propertymanagement.core.designsystem.theme.PmTheme
import org.stkachenko.propertymanagement.core.model.data.property.Property

fun propertiesList(
    propertyListState: PropertiesUiState,
    onPropertyClick: () -> Unit,
) {
    when (propertyListState) {
        PropertiesUiState.Loading -> Unit
        is PropertiesUiState.Success -> {
            items(
                items = propertyListState.properties,
                key = { it.id },
                contentType = { "propertyItem" },
            ) { property ->
                PropertySmallCard(
                    property = property,
                    onClick = onPropertyClick,
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
        LazyHorizontalStaggeredGrid(rows = StaggeredGridCells.Fixed(1)) {
            propertiesList(
                propertyListState = PropertiesUiState.Loading,
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
        LazyHorizontalStaggeredGrid(rows = StaggeredGridCells.Fixed(1)) {
            propertiesList(
                propertyListState = PropertiesUiState.Success(propertiesList),
                onPropertyClick = {},
            )
        }
    }
}