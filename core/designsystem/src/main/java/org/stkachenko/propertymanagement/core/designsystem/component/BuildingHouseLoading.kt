package org.stkachenko.propertymanagement.core.designsystem.component

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import org.stkachenko.propertymanagement.core.designsystem.theme.PmTheme

private const val HOUSE_PARTS = 5
private const val BUILD_TIME = 4000 // ms для повного циклу

@Composable
fun PmBuildingHouseLoading(
    contentDesc: String,
    modifier: Modifier = Modifier,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "house transition")

    // прогрес кожної частини
    val partProgress = (0 until HOUSE_PARTS).map { index ->
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = BUILD_TIME
                    0f at 0
                    1f at BUILD_TIME / HOUSE_PARTS
                    1f at BUILD_TIME
                },
                repeatMode = RepeatMode.Restart,
                initialStartOffset = StartOffset(index * BUILD_TIME / HOUSE_PARTS),
            ),
            label = "house part $index",
        )
    }

    val baseLineColor = MaterialTheme.colorScheme.onBackground
    val progressLineColor = MaterialTheme.colorScheme.inversePrimary

    Canvas(
        modifier = modifier
            .size(48.dp)
            .padding(8.dp)
            .semantics { contentDescription = contentDesc }
            .testTag("buildingHouseWheel"),
    ) {
        val w = size.width
        val h = size.height

        // === Part 1: ліва частина фундаменту + шматок лівої стіни ===
        drawRect(
            color = baseLineColor,
            topLeft = Offset(w * 0.2f, h * 0.7f),
            size = Size(w * 0.3f, partProgress[0].value * h * 0.1f),
        )
        drawRect(
            color = baseLineColor,
            topLeft = Offset(w * 0.2f, h * 0.5f),
            size = Size(w * 0.05f, partProgress[0].value * h * 0.2f),
        )

        // === Part 2: права частина фундаменту + права стіна ===
        drawRect(
            color = baseLineColor,
            topLeft = Offset(w * 0.5f, h * 0.7f),
            size = Size(w * 0.3f, partProgress[1].value * h * 0.1f),
        )
        drawRect(
            color = baseLineColor,
            topLeft = Offset(w * 0.75f, h * 0.5f),
            size = Size(w * 0.05f, partProgress[1].value * h * 0.2f),
        )

        // === Part 3: завершення лівої стіни + шматок стелі + трохи даху ===
        drawRect(
            color = baseLineColor,
            topLeft = Offset(w * 0.2f, h * 0.3f),
            size = Size(w * 0.05f, partProgress[2].value * h * 0.2f),
        )
        drawRect(
            color = baseLineColor,
            topLeft = Offset(w * 0.25f, h * 0.3f),
            size = Size(w * 0.5f, partProgress[2].value * h * 0.05f),
        )
        if (partProgress[2].value > 0f) {
            val path = Path().apply {
                moveTo(w * 0.25f, h * 0.3f)
                lineTo(w * 0.5f, h * (0.15f + 0.1f * (1 - partProgress[2].value)))
                lineTo(w * 0.5f, h * 0.3f)
                close()
            }
            drawPath(path, progressLineColor)
        }

        // === Part 4: завершення правої стіни + друга половина стелі + ще трохи даху ===
        drawRect(
            color = baseLineColor,
            topLeft = Offset(w * 0.75f, h * 0.3f),
            size = Size(w * 0.05f, partProgress[3].value * h * 0.2f),
        )
        drawRect(
            color = baseLineColor,
            topLeft = Offset(w * 0.25f, h * 0.25f),
            size = Size(w * 0.5f, partProgress[3].value * h * 0.05f),
        )
        if (partProgress[3].value > 0f) {
            val path = Path().apply {
                moveTo(w * 0.5f, h * 0.15f)
                lineTo(w * 0.75f, h * 0.3f)
                lineTo(w * 0.5f, h * (0.15f + 0.1f * (1 - partProgress[3].value)))
                close()
            }
            drawPath(path, progressLineColor)
        }

        // === Part 5: решта даху ===
        if (partProgress[4].value > 0f) {
            val path = Path().apply {
                moveTo(w * 0.25f, h * 0.3f)
                lineTo(w * 0.75f, h * 0.3f)
                lineTo(w * 0.5f, h * 0.15f)
                close()
            }
            drawPath(path, progressLineColor.copy(alpha = partProgress[4].value))
        }
    }
}

@Composable
fun PmOverlayBuildingHouseLoading(
    contentDesc: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(60.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.83f),
        modifier = modifier.size(60.dp),
    ) {
        PmBuildingHouseLoading(contentDesc = contentDesc)
    }
}


@ThemePreviews
@Composable
fun PmLoadingHousePreview() {
    PmTheme {
        Surface {
            PmBuildingHouseLoading(contentDesc = "LoadingWheel")
        }
    }
}

@ThemePreviews
@Composable
fun PmOverlayLoadingHousePreview() {
    PmTheme {
        Surface {
            PmOverlayBuildingHouseLoading(contentDesc = "LoadingWheel")
        }
    }
}
