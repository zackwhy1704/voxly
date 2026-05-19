package com.voxly.shared.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voxly.shared.domain.model.NodeState
import com.voxly.shared.ui.theme.VoxlyColors

@Composable
fun LearningPathNode(
    nodeNumber: Int,
    state: NodeState,
    accentColor: Color,
    onTap: () -> Unit,
    modifier: Modifier = Modifier,
    size: Int = 64,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "node_pulse")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "scale",
    )

    val (bg, alpha, icon) = when (state) {
        NodeState.LOCKED      -> Triple(accentColor, 0.15f, "🔒")
        NodeState.AVAILABLE   -> Triple(accentColor, 0.4f,  nodeNumber.toString())
        NodeState.IN_PROGRESS -> Triple(accentColor, 1f,    "▶")
        NodeState.COMPLETED   -> Triple(accentColor, 1f,    "✓")
    }

    val scale = if (state == NodeState.IN_PROGRESS) pulse else 1f

    Box(
        modifier = modifier
            .size(size.dp)
            .scale(scale)
            .clip(CircleShape)
            .background(bg.copy(alpha = alpha))
            .clickable(
                enabled = state != NodeState.LOCKED,
                onClick = onTap,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = icon,
            fontSize = (size * 0.38f).sp,
            fontWeight = FontWeight.Bold,
            color = when (state) {
                NodeState.LOCKED    -> VoxlyColors.TextTertiary
                NodeState.AVAILABLE -> accentColor
                else                -> Color.White
            },
        )
    }
}
