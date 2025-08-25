package org.stkachenko.propertymanagement.core.ui

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
import org.stkachenko.propertymanagement.core.designsystem.R.drawable
import org.stkachenko.propertymanagement.core.designsystem.theme.PmTheme
import org.stkachenko.propertymanagement.core.model.data.property.Property

@Composable
fun PropertySmallCard(
    property: Property,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val clickActionLabel = stringResource(id = R.string.core_ui__small_card_tap_action)
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier.semantics {
            onClick(label = clickActionLabel, action = null)
        },
    ) {
        Column {
            if (property.images.isNotEmpty()) {
                Row {
                    UserPropertyHeaderImage(
                        property.images.firstOrNull { it.position == 0 }?.url ?: ""
                    )
                }
            }
            Box(
                modifier = Modifier.padding(16.dp)
            ) {
                Column {
                    PropertyStreet(property.address.getOrDefault("street", ""))
                    PropertyCity(property.address.getOrDefault("city", ""))
                }
            }
        }
    }
}

@Composable
fun PropertyStreet(
    propertyStreet: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = propertyStreet,
        style = MaterialTheme.typography.titleMedium,
        modifier = modifier
    )
}

@Composable
fun PropertyCity(
    propertyCity: String,
) {
    Text(
        text = propertyCity,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
fun UserPropertyHeaderImage(
    headerImageUrl: String?,
) {
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    val imageLoader = rememberAsyncImagePainter(
        model = headerImageUrl,
        onState = { state ->
            isLoading = state is AsyncImagePainter.State.Loading
            isError = state is AsyncImagePainter.State.Error
        },
    )
    val isLocalInspection = LocalInspectionMode.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(80.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }

        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            contentScale = ContentScale.Crop,
            painter = if (!isError && !isLocalInspection) {
                imageLoader
            } else {
                painterResource(drawable.core_designsystem_ic_placeholder_default)
            },
            contentDescription = null
        )
    }
}

@Preview
@Composable
fun PropertySmallCardPreview(
    @PreviewParameter(UserPropertyPreviewParameterProvider::class)
    userProperties: List<Property>,
) {
    CompositionLocalProvider(
        LocalInspectionMode provides true,
    ) {
        PmTheme {
            Surface {
                PropertySmallCard(
                    property = userProperties.first(),
                    onClick = {},
                )
            }
        }
    }
}