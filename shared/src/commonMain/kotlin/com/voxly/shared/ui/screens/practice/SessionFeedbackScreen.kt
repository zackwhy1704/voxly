package com.voxly.shared.ui.screens.practice

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.voxly.shared.domain.model.ScoreBreakdown
import com.voxly.shared.ui.components.*
import com.voxly.shared.ui.theme.VoxlyColors

class SessionFeedbackScreen(private val sessionId: String) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val scores = ScoreBreakdown(pronunciation = 82, fluency = 68, grammar = 79, vocabulary = 71)
        val insights = listOf(
            "✓  Strong use of STAR method throughout",
            "⚠️  Filler words detected: 2 instances of 'basically'",
            "💡  Slow down slightly on technical terms for clarity",
            "🎯  Try more assertive closings on behavioural questions",
        )

        Column(
            modifier = Modifier.fillMaxSize().background(VoxlyColors.Background)
                .statusBarsPadding().navigationBarsPadding()
                .verticalScroll(rememberScrollState()).padding(horizontal = 20.dp),
        ) {
            Spacer(Modifier.height(24.dp))
            Text("Session Complete 🎉", fontSize = 22.sp, fontWeight = FontWeight.Bold,
                color = VoxlyColors.TextPrimary, textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth())
            Text("18 min · 8 questions answered", fontSize = 13.sp, color = VoxlyColors.TextSecondary,
                textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())

            Spacer(Modifier.height(24.dp))

            // Score circle
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Box(modifier = Modifier.size(160.dp).clip(CircleShape)
                    .background(VoxlyColors.Coral.copy(alpha = 0.06f)))
                Box(modifier = Modifier.size(120.dp).clip(CircleShape)
                    .background(VoxlyColors.Coral.copy(alpha = 0.1f)))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text("${scores.overall}", fontSize = 40.sp, fontWeight = FontWeight.Bold,
                            color = VoxlyColors.Coral)
                        Text("/100", fontSize = 16.sp, color = VoxlyColors.TextTertiary,
                            modifier = Modifier.padding(bottom = 4.dp))
                    }
                    Text("Personal best +8pts", fontSize = 11.sp, color = VoxlyColors.Green)
                }
            }

            Spacer(Modifier.height(24.dp))

            // Score breakdown
            Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp))
                .background(VoxlyColors.SurfaceElevated).padding(16.dp)) {
                ProgressBar4D(scores = scores)
            }

            Spacer(Modifier.height(16.dp))

            // AI insights
            Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp))
                .background(VoxlyColors.SurfaceElevated).padding(16.dp)) {
                Column {
                    Text("AI Coach Insights", fontSize = 14.sp, color = VoxlyColors.Teal,
                        fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(10.dp))
                    insights.forEach { insight ->
                        Text(insight, fontSize = 13.sp, color = VoxlyColors.TextSecondary,
                            lineHeight = 20.sp, modifier = Modifier.padding(vertical = 3.dp))
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // XP strip
            Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                .background(VoxlyColors.Gold10).padding(16.dp)) {
                Text("⚡ +45 XP · 🔥 Streak 13 days · Top 17%",
                    fontSize = 13.sp, color = VoxlyColors.Gold, fontWeight = FontWeight.SemiBold)
            }

            Spacer(Modifier.height(20.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                VoxlyButton("Try Again", onClick = { navigator.pop() },
                    variant = VoxlyButtonVariant.Ghost, modifier = Modifier.weight(1f))
                VoxlyButton("Next Scenario", onClick = { navigator.popUntilRoot() },
                    modifier = Modifier.weight(1f))
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}
