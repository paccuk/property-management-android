package org.stkachenko.propertymanagement.core.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import org.stkachenko.propertymanagement.core.designsystem.theme.PmTheme
import org.stkachenko.propertymanagement.core.model.data.property.Property

fun LazyStaggeredGridScope.propertiesList(
    propertyListState: PropertyListUiState,
    onPropertyClick: () -> Unit,
) {
    when (propertyListState) {
        PropertyListUiState.Loading -> Unit
        is PropertyListUiState.Success -> {
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

sealed interface PropertyListUiState {

    data object Loading : PropertyListUiState

    data class Success(val properties: List<Property>) : PropertyListUiState
}

@Preview
@Composable
private fun PropertiesListLoadingPreview() {
    PmTheme {
        LazyHorizontalStaggeredGrid(rows = StaggeredGridCells.Fixed(1)) {
            propertiesList(
                propertyListState = PropertyListUiState.Loading,
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
                propertyListState = PropertyListUiState.Success(propertiesList),
                onPropertyClick = {},
            )
        }
    }
}