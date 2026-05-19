package com.voxly.shared.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voxly.shared.domain.model.Scenario
import com.voxly.shared.ui.theme.VoxlyColors

@Composable
fun ScenarioCard(
    scenario: Scenario,
    onTap: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val accentColor = Color(scenario.accentColorHex)
    val shape = RoundedCornerShape(18.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(102.dp)
            .clip(shape)
            .background(VoxlyColors.SurfaceElevated)
            .clickable(onClick = onTap),
    ) {
        // Left accent bar
        Box(
            modifier = Modifier
                .width(4.dp)
                .fillMaxHeight()
                .background(accentColor),
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Icon circle
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(androidx.compose.foundation.shape.CircleShape)
                    .background(accentColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = scenario.category.displayName.take(1), fontSize = 18.sp)
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = scenario.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = VoxlyColors.TextPrimary,
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = scenario.subtitle,
                    fontSize = 12.sp,
                    color = VoxlyColors.TextSecondary,
                )
                if (scenario.progressFraction > 0f) {
                    Spacer(Modifier.height(6.dp))
                    VoxlyProgressBar(
                        progress = scenario.progressFraction,
                        color = accentColor,
                        modifier = Modifier.fillMaxWidth(0.7f),
                    )
                }
            }

            Spacer(Modifier.width(8.dp))

            VoxlyButton(
                label = if (scenario.progressFraction > 0f) "Resume" else "Start",
                onClick = onTap,
                variant = VoxlyButtonVariant.Secondary,
                modifier = Modifier.width(72.dp),
                height = 30,
            )
        }
    }
}

@Composable
fun VoxlyProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = VoxlyColors.Coral,
    trackColor: Color = VoxlyColors.White12,
    height: Int = 4,
) {
    Box(
        modifier = modifier
            .height(height.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(trackColor),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .fillMaxHeight()
                .background(color),
        )
    }
}
