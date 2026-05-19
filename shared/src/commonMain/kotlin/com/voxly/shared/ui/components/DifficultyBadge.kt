package com.voxly.shared.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voxly.shared.domain.model.DifficultyTier
import com.voxly.shared.ui.theme.VoxlyColors

@Composable
fun DifficultyBadge(
    tier: DifficultyTier,
    modifier: Modifier = Modifier,
    isLocked: Boolean = false,
    size: Int = 48,
) {
    val tierColor = Color(tier.colorHex)

    Box(
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(
                if (isLocked) tierColor.copy(alpha = 0.15f) else tierColor.copy(alpha = 0.2f),
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (isLocked) {
            Text(text = "🔒", fontSize = (size * 0.4f).sp)
        } else {
            Text(
                text = tier.displayName.take(1),
                fontSize = (size * 0.45f).sp,
                fontWeight = FontWeight.Bold,
                color = if (isLocked) VoxlyColors.TextTertiary else tierColor,
            )
        }
    }
}
