package com.voxly.shared.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voxly.shared.domain.model.ScoreBreakdown
import com.voxly.shared.ui.theme.VoxlyColors

@Composable
fun ProgressBar4D(
    scores: ScoreBreakdown,
    modifier: Modifier = Modifier,
) {
    val items = listOf(
        Triple("Pronunciation", scores.pronunciation, VoxlyColors.Teal),
        Triple("Fluency",       scores.fluency,       VoxlyColors.Coral),
        Triple("Grammar",       scores.grammar,       VoxlyColors.Green),
        Triple("Vocabulary",    scores.vocabulary,    VoxlyColors.Gold),
    )

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items.forEach { (label, score, color) ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = label,
                    fontSize = 13.sp,
                    color = VoxlyColors.TextSecondary,
                    modifier = Modifier.width(100.dp),
                )
                VoxlyProgressBar(
                    progress = score / 100f,
                    color = color,
                    modifier = Modifier.weight(1f),
                    height = 6,
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "$score%",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = color,
                    modifier = Modifier.width(36.dp),
                )
            }
        }
    }
}
