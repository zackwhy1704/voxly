package com.voxly.shared.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.voxly.shared.ui.theme.VoxlyColors
import kotlin.math.sin

@Composable
fun PronunciationWaveform(
    amplitudes: List<Float>,
    modifier: Modifier = Modifier,
    isActive: Boolean = false,
    accentColor: Color = VoxlyColors.Coral,
    barCount: Int = 24,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "waveform")
    val animPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * 3.14159f,
        animationSpec = infiniteRepeatable(
            tween(1500, easing = LinearEasing),
        ),
        label = "phase",
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
    ) {
        val barWidth = (size.width - (barCount - 1) * 4.dp.toPx()) / barCount
        val maxBarHeight = size.height

        repeat(barCount) { i ->
            val fraction = i.toFloat() / barCount
            val amplitude = if (amplitudes.size > i) {
                amplitudes[i].coerceIn(0f, 1f)
            } else if (isActive) {
                // Animated idle wave when active but no real data
                (0.3f + 0.4f * sin(fraction * 6 + animPhase)).coerceIn(0.1f, 1f)
            } else {
                0.15f
            }

            val barHeight = (amplitude * maxBarHeight).coerceAtLeast(4.dp.toPx())
            val x = i * (barWidth + 4.dp.toPx())
            val y = (maxBarHeight - barHeight) / 2

            drawRoundRect(
                color = accentColor.copy(alpha = 0.4f + 0.6f * amplitude),
                topLeft = Offset(x, y),
                size = Size(barWidth, barHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(2.dp.toPx()),
            )
        }
    }
}
