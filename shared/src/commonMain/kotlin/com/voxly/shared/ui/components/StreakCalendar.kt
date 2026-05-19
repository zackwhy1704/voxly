package com.voxly.shared.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voxly.shared.ui.theme.VoxlyColors

enum class DayOfWeek(val label: String) {
    MON("M"), TUE("T"), WED("W"), THU("T"), FRI("F"), SAT("S"), SUN("S")
}

@Composable
fun StreakCalendar(
    completedDays: List<DayOfWeek>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        DayOfWeek.entries.forEach { day ->
            val isDone = day in completedDays
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .then(
                        if (isDone)
                            Modifier.background(VoxlyColors.Coral)
                        else
                            Modifier.border(1.5.dp, VoxlyColors.White12, CircleShape)
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = day.label,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isDone) androidx.compose.ui.graphics.Color.White
                            else VoxlyColors.TextTertiary,
                )
            }
        }
    }
}
