package org.stkachenko.propertymanagement.feature.home

import androidx.activity.compose.ReportDrawnWhen
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.stkachenko.propertymanagement.core.designsystem.component.scrollbar.scrollbarState

@Composable
internal fun HomeRoute(
    onPropertyClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val propertiesState by viewModel.propertiesUiState.collectAsStateWithLifecycle()
    val isSyncing by viewModel.isSyncing.collectAsStateWithLifecycle()

    HomeScreen(
        isSyncing = isSyncing,
        propertiesState = propertiesState,
        onPropertyClick = onPropertyClick,
        modifier = modifier,
    )
}

@Composable
internal fun HomeScreen(
    isSyncing: Boolean,
    propertiesState: PropertiesUiState,
    onPropertyClick: (String) -> Unit,
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
            columns = StaggeredGridCells.Adaptive(300.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalItemSpacing = 24.dp,
            modifier = Modifier
                .testTag("home:properties_list"),
            state = state,
        ) {

        }
    }
}

private fun propertyItemsSize(propertiesState: PropertiesUiState): Int =
    when (propertiesState) {
        is PropertiesUiState.Loading -> 0
        is PropertiesUiState.Success -> propertiesState.properties.size

    }

@Composable
fun HeaderSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(id = R.string.feature_home_title),
            style = MaterialTheme.typography.headlineMedium,
        )
        IconButton(
            onClick = { /* TODO: Open notifications */ }) {
            Icon(
                painter = painterResource(id = drawable.ic_notifications),
                contentDescription = "Notifications"
            )
        }
    }
}

@Composable
fun PropertiesSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(text = "Properties", style = MaterialTheme.typography.titleLarge)
        // TODO: Replace with LazyRow for horizontal scrolling of properties
    }

//    LazyVerticalStaggeredGrid(
//        columns = StaggeredGridCells.Fixed(1),
//        modifier = Modifier.fillMaxSize(),
//        contentPadding = PaddingValues(vertical = 8.dp)
//    ) {
//        item { PropertiesSection() }
//        item { StatisticsSection() }
//    }
}

@Composable
fun StatisticsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(text = "Statistics", style = MaterialTheme.typography.titleLarge)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Monthly Earnings", style = MaterialTheme.typography.bodyLarge)
                Text(text = "$23.2k", style = MaterialTheme.typography.headlineMedium)
                Text(text = "Last 30 Days +12%", color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(8.dp))
                // Placeholder for Graph
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                ) {
                    Text(text = "[Graph Placeholder]", modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewHomeScreen() {
    HomeScreen()
}

