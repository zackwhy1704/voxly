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
import com.voxly.shared.domain.model.HUDState
import com.voxly.shared.ui.theme.VoxlyColors

@Composable
fun SessionHUD(
    state: HUDState,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(VoxlyColors.SurfaceElevated)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HUDItem(label = "Pace", value = state.pace)
        HUDDivider()
        HUDItem(label = "Fillers", value = state.fillerCount.toString())
        HUDDivider()
        HUDItem(label = "Clarity", value = state.clarity)
        HUDDivider()
        HUDItem(
            label = "Time",
            value = formatTime(state.elapsedSeconds),
        )
    }
}

@Composable
private fun HUDItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = VoxlyColors.Teal,
        )
        Text(text = label, fontSize = 10.sp, color = VoxlyColors.TextTertiary)
    }
}

@Composable
private fun HUDDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(24.dp)
            .background(VoxlyColors.White12),
    )
}

private fun formatTime(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return "${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}"
}
