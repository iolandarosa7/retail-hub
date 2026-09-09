/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.ui.progress

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.iolandarosa.retailhub.core.ui.theme.Dimens

@Composable
fun Skeleton(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(Dimens.CornerRadiusSmall),
    contentDescription: String = "Loading",
) {
    val base = MaterialTheme.colorScheme.surfaceVariant
    val highlight = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)

    val transition = rememberInfiniteTransition(label = "skeleton")

    val offset by transition.animateFloat(
        initialValue = -1f,
        targetValue = 2f,
        animationSpec =
            infiniteRepeatable(
                tween(
                    durationMillis = 1200,
                    easing = LinearEasing,
                ),
                RepeatMode.Restart,
            ),
        label = "shimmer",
    )

    val brush =
        Brush.linearGradient(
            colors =
                listOf(
                    base.copy(alpha = 0.7f),
                    highlight.copy(alpha = 0.95f),
                    base.copy(alpha = 0.7f),
                ),
            start = Offset(offset * 500f, 0f),
            end = Offset(offset * 500f + 200f, 0f),
        )

    Box(
        modifier =
            modifier
                .clip(shape)
                .background(brush)
                .semantics { this.contentDescription = contentDescription },
    )
}
