package com.voxly.shared.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voxly.shared.ui.theme.VoxlyColors

@Composable
fun CareerReadinessCard(
    score: Int,
    tier: String,
    rank: String,
    deltaThisMonth: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(VoxlyColors.SurfaceElevated)
            .padding(20.dp),
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Career Readiness",
                    fontSize = 12.sp,
                    color = VoxlyColors.TextSecondary.copy(alpha = 0.45f),
                )
                // Tier pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(VoxlyColors.White06)
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                ) {
                    Text(text = "🥈 $tier", fontSize = 12.sp, color = VoxlyColors.TextSecondary)
                }
            }

            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = score.toString(),
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Bold,
                    color = VoxlyColors.Coral,
                )
                Text(
                    text = "/100",
                    fontSize = 18.sp,
                    color = VoxlyColors.TextTertiary,
                    modifier = Modifier.padding(bottom = 4.dp, start = 2.dp),
                )
                Spacer(Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(VoxlyColors.Green.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                ) {
                    Text(
                        text = "+$deltaThisMonth this month",
                        fontSize = 11.sp,
                        color = VoxlyColors.Green,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

            Text(text = rank, fontSize = 13.sp, color = VoxlyColors.TextSecondary)

            Spacer(Modifier.height(12.dp))

            VoxlyProgressBar(
                progress = score / 100f,
                color = VoxlyColors.Coral,
                modifier = Modifier.fillMaxWidth(),
                height = 6,
            )
        }
    }
}
