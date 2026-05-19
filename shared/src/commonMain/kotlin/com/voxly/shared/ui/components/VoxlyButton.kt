package com.voxly.shared.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voxly.shared.ui.theme.VoxlyColors

enum class VoxlyButtonVariant { Primary, Secondary, Ghost }

@Composable
fun VoxlyButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: VoxlyButtonVariant = VoxlyButtonVariant.Primary,
    isLoading: Boolean = false,
    isEnabled: Boolean = true,
    height: Int = 56,
) {
    val shape = RoundedCornerShape(16.dp)

    val (containerColor, contentColor, borderColor) = when (variant) {
        VoxlyButtonVariant.Primary   -> Triple(
            if (isEnabled) VoxlyColors.Coral else VoxlyColors.White12,
            if (isEnabled) Color.White else VoxlyColors.TextTertiary,
            Color.Transparent,
        )
        VoxlyButtonVariant.Secondary -> Triple(
            VoxlyColors.SurfaceElevated,
            VoxlyColors.TextPrimary,
            Color.Transparent,
        )
        VoxlyButtonVariant.Ghost     -> Triple(
            Color.Transparent,
            VoxlyColors.Coral,
            VoxlyColors.White12,
        )
    }

    Button(
        onClick = onClick,
        enabled = isEnabled && !isLoading,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor         = containerColor,
            contentColor           = contentColor,
            disabledContainerColor = VoxlyColors.White06,
            disabledContentColor   = VoxlyColors.TextTertiary,
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(height.dp)
            .then(
                if (borderColor != Color.Transparent)
                    Modifier.border(1.dp, borderColor, shape)
                else Modifier
            ),
        contentPadding = PaddingValues(horizontal = 24.dp),
    ) {
        if (isLoading) {
            val infiniteTransition = rememberInfiniteTransition(label = "loading")
            val angle by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(tween(800, easing = LinearEasing)),
                label = "angle",
            )
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = contentColor,
                strokeWidth = 2.dp,
            )
        } else {
            Text(
                text = label,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = contentColor,
            )
        }
    }
}
