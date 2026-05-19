package com.voxly.shared.ui.screens.practice

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import com.voxly.shared.ui.components.VoxlyButton
import com.voxly.shared.ui.theme.VoxlyColors

class SessionBriefScreen(private val scenarioId: String) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var warmupEnabled by remember { mutableStateOf(true) }

        Column(
            modifier = Modifier.fillMaxSize().background(VoxlyColors.Background)
                .statusBarsPadding().navigationBarsPadding()
                .verticalScroll(rememberScrollState()),
        ) {
            // Header
            Box(modifier = Modifier.fillMaxWidth().background(VoxlyColors.Surface)
                .padding(horizontal = 20.dp, vertical = 16.dp)) {
                Text("←", fontSize = 20.sp, color = VoxlyColors.TextPrimary,
                    modifier = Modifier.align(Alignment.CenterStart).clickable { navigator.pop() })
                Text("Tech Interview Prep", fontSize = 17.sp, fontWeight = FontWeight.SemiBold,
                    color = VoxlyColors.TextPrimary, modifier = Modifier.align(Alignment.Center))
            }

            // Hero glow + icon
            Box(modifier = Modifier.fillMaxWidth().height(200.dp),
                contentAlignment = Alignment.Center) {
                Box(modifier = Modifier.size(160.dp).clip(CircleShape)
                    .background(VoxlyColors.Coral.copy(alpha = 0.06f)))
                Box(modifier = Modifier.size(100.dp).clip(CircleShape)
                    .background(VoxlyColors.Coral.copy(alpha = 0.1f)))
                Text("💼", fontSize = 48.sp)
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text("Senior Engineer", fontSize = 22.sp, fontWeight = FontWeight.Bold,
                    color = VoxlyColors.TextPrimary, textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth())
                Text("Google SG · 18 min", fontSize = 14.sp, color = VoxlyColors.TextSecondary,
                    textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())

                Spacer(Modifier.height(12.dp))

                // Info pills
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    InfoPill("⏱ 18 min")
                    InfoPill("🔥 Hard")
                    InfoPill("🇸🇬 SEA context")
                }

                Spacer(Modifier.height(16.dp))

                // Scenario context card
                Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp))
                    .background(VoxlyColors.SurfaceElevated).padding(16.dp)) {
                    Column {
                        Text("Scenario context", fontSize = 13.sp, color = VoxlyColors.Teal,
                            fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "You're interviewing for a senior backend role at Google Singapore. " +
                                "The interviewer is a local engineering lead. Expect technical depth + " +
                                "behavioural questions. Use STAR method. Demonstrate professional SG English.",
                            fontSize = 13.sp, color = VoxlyColors.TextSecondary, lineHeight = 20.sp,
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                // AI Coach tip
                Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp))
                    .background(VoxlyColors.Teal08).padding(16.dp)) {
                    Column {
                        Text("AI Coach tip", fontSize = 12.sp, color = VoxlyColors.Teal,
                            fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(4.dp))
                        Text("Grammar 78% last session. Focus on fluency and sentence completion today.",
                            fontSize = 13.sp, color = VoxlyColors.TextSecondary, lineHeight = 20.sp)
                    }
                }

                Spacer(Modifier.height(12.dp))

                // Warm-up toggle
                Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp))
                    .background(VoxlyColors.SurfaceElevated)
                    .padding(horizontal = 16.dp, vertical = 12.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically) {
                        Text("Warm-up exercise first", fontSize = 14.sp, color = VoxlyColors.TextPrimary)
                        Switch(
                            checked = warmupEnabled,
                            onCheckedChange = { warmupEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = androidx.compose.ui.graphics.Color.White,
                                checkedTrackColor = VoxlyColors.Coral,
                            ),
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                VoxlyButton("Start Session", onClick = { navigator.push(SessionActiveScreen(scenarioId)) })
                Spacer(Modifier.height(8.dp))
                Text("+45 XP on completion", fontSize = 12.sp, color = VoxlyColors.TextTertiary,
                    textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun InfoPill(text: String) {
    Box(modifier = Modifier.clip(RoundedCornerShape(20.dp))
        .background(VoxlyColors.SurfaceElevated).padding(horizontal = 10.dp, vertical = 5.dp)) {
        Text(text, fontSize = 12.sp, color = VoxlyColors.TextSecondary)
    }
}
