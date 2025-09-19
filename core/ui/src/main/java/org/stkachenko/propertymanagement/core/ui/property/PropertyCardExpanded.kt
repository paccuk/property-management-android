package org.stkachenko.propertymanagement.core.ui.property

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import org.stkachenko.propertymanagement.core.designsystem.theme.PmTheme
import org.stkachenko.propertymanagement.core.model.data.image.Image
import org.stkachenko.propertymanagement.core.model.data.property.Property
import org.stkachenko.propertymanagement.core.ui.R
import org.stkachenko.propertymanagement.core.designsystem.R.drawable as drawable

@Composable
fun PropertyCardExpanded(
    property: Property,
    onPropertyClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val clickActionLabel = stringResource(R.string.core_ui_property_card_expanded_tap_action)

    Card(
        onClick = { onPropertyClick(property.id) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = modifier.semantics {
            onClick(label = clickActionLabel, action = null)
        }
    ) {
        Column {
            Row {
                PropertyCardHeaderImage(
                    property.images.firstOrNull { it.position == 0 }
                )
            }
            Box(
                modifier = Modifier.padding(16.dp)
            ) {
                Column {
                    Row {
                        PropertyAddress(
                            property.address,
                            modifier = Modifier
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        PropertyPrice(
                            property.price,
                            modifier = Modifier
                        )
                        Spacer(modifier = Modifier.size(4.dp))
                        PropertyCurrency(
                            property.currency,
                            modifier = Modifier
                        )

                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        PropertyDetails(
                            property.type,
                            modifier = Modifier
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PropertyAddress(
    propertyAddress: Map<String, String>,
    modifier: Modifier = Modifier,
) {
    Text(
        text = propertyAddress.getOrDefault("street", "") + ", " +
                propertyAddress.getOrDefault("city", ""),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = modifier
    )
}

@Composable
fun PropertyPrice(
    propertyPrice: Double,
    modifier: Modifier = Modifier,
) {
    Text(
        text = propertyPrice.toString(),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier
    )
}

@Composable
fun PropertyCurrency(
    propertyCurrency: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = propertyCurrency,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier
    )
}

@Composable
fun PropertyDetails(
    propertyType: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = propertyType,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = modifier
    )
}

@Composable
fun PropertyCardHeaderImage(
    headerImageUrl: Image?,
) {
    if (headerImageUrl != null) {
        var isLoading by remember { mutableStateOf(true) }
        var isError by remember { mutableStateOf(false) }
        val imageLoader = rememberAsyncImagePainter(
            model = headerImageUrl.url,
            onState = { state ->
                isLoading = state is AsyncImagePainter.State.Loading
                isError = state is AsyncImagePainter.State.Error
            },
        )
        val isLocalInspection = LocalInspectionMode.current
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            contentAlignment = Alignment.Center,
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(80.dp),
                    color = MaterialTheme.colorScheme.tertiary,
                )
            }

            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop,
                painter = if (isError.not() && !isLocalInspection) {
                    imageLoader
                } else {
                    painterResource(drawable.core_designsystem_ic_placeholder_default)
                },
                contentDescription = null,
            )
        }
    } else {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            contentScale = ContentScale.Crop,
            painter = painterResource(drawable.core_designsystem_ic_placeholder_default),
            contentDescription = null,
        )
    }
}

@Preview
@Composable
fun PropertyCardExpandedPreview(
    @PreviewParameter(UserPropertyPreviewParameterProvider::class)
    userProperties: List<Property>,
) {
    CompositionLocalProvider(
        LocalInspectionMode provides true
    ) {
        PmTheme {
            Surface {
                PropertyCardExpanded(
                    property = userProperties.first(),
                    onPropertyClick = {},
                )
            }
        }
    }
}