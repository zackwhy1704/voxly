package com.voxly.shared.ui.screens.practice

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.voxly.shared.domain.model.HUDState
import com.voxly.shared.ui.components.*
import com.voxly.shared.ui.theme.VoxlyColors
import kotlinx.coroutines.delay

class SessionActiveScreen(private val scenarioId: String) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var elapsedSeconds by remember { mutableIntStateOf(0) }
        var isListening by remember { mutableStateOf(true) }
        var isPaused by remember { mutableStateOf(false) }
        var currentQ by remember { mutableIntStateOf(3) }
        val totalQ = 8

        val amplitudes by remember {
            mutableStateOf(
                listOf(0.3f, 0.5f, 0.8f, 0.6f, 0.9f, 0.4f, 0.7f, 0.5f,
                       0.3f, 0.8f, 0.9f, 0.6f, 0.5f, 0.7f, 0.4f, 0.6f,
                       0.3f, 0.9f, 0.7f, 0.5f, 0.4f, 0.8f, 0.6f, 0.3f)
            )
        }

        val hud = HUDState(pace = "good", fillerCount = 2, clarity = "high",
            elapsedSeconds = elapsedSeconds)

        LaunchedEffect(isPaused) {
            if (!isPaused) {
                while (true) { delay(1000); elapsedSeconds++ }
            }
        }

        val infiniteTransition = rememberInfiniteTransition(label = "mic_pulse")
        val outerScale by infiniteTransition.animateFloat(
            initialValue = 1f, targetValue = 1.12f,
            animationSpec = infiniteRepeatable(tween(800), RepeatMode.Reverse),
            label = "outer",
        )

        Box(
            modifier = Modifier.fillMaxSize().background(VoxlyColors.Background)
                .statusBarsPadding().navigationBarsPadding(),
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
                Spacer(Modifier.height(16.dp))

                // Question card
                Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp))
                    .background(VoxlyColors.SurfaceElevated).padding(16.dp)) {
                    Column {
                        Text("Q$currentQ of $totalQ", fontSize = 12.sp,
                            color = VoxlyColors.TextTertiary)
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "\"Tell me about a time you influenced a team without authority.\"",
                            fontSize = 15.sp, color = VoxlyColors.TextPrimary, lineHeight = 22.sp,
                        )
                        Spacer(Modifier.height(10.dp))
                        VoxlyProgressBar(
                            progress = currentQ.toFloat() / totalQ,
                            color = VoxlyColors.Coral,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }

                // Concentric rings + mic
                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentAlignment = Alignment.Center,
                ) {
                    // Outer ring
                    Box(modifier = Modifier.size(200.dp).scale(if (isListening) outerScale else 1f)
                        .clip(CircleShape).background(VoxlyColors.Coral.copy(alpha = 0.06f)))
                    Box(modifier = Modifier.size(160.dp).clip(CircleShape)
                        .background(VoxlyColors.Coral.copy(alpha = 0.09f)))
                    Box(modifier = Modifier.size(120.dp).clip(CircleShape)
                        .background(VoxlyColors.Coral.copy(alpha = 0.15f)))

                    // Mic circle
                    Box(modifier = Modifier.size(80.dp).clip(CircleShape)
                        .background(VoxlyColors.Coral).clickable { isPaused = !isPaused },
                        contentAlignment = Alignment.Center) {
                        Text(if (isPaused) "⏸" else "🎙", fontSize = 28.sp)
                    }
                }

                // Timer
                Text(
                    text = formatTime(elapsedSeconds),
                    fontSize = 32.sp, fontWeight = FontWeight.Bold,
                    color = VoxlyColors.TextPrimary, textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(4.dp))
                if (isListening && !isPaused) {
                    Text("LISTENING...", fontSize = 13.sp, color = VoxlyColors.Coral,
                        fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth())
                }

                Spacer(Modifier.height(12.dp))

                // Waveform
                PronunciationWaveform(
                    amplitudes = if (isListening && !isPaused) amplitudes else emptyList(),
                    isActive = isListening && !isPaused,
                    accentColor = VoxlyColors.Coral,
                )

                Spacer(Modifier.height(12.dp))

                // HUD
                SessionHUD(state = hud)

                Spacer(Modifier.height(20.dp))

                // Controls
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically) {
                    ControlButton("⏸", "Pause") { isPaused = !isPaused }
                    ControlButton("⏭", "Skip") { currentQ = (currentQ + 1).coerceAtMost(totalQ) }
                    ControlButton("🔴", "End", tint = VoxlyColors.Error) {
                        navigator.push(SessionFeedbackScreen("session_${scenarioId}"))
                    }
                }

                Spacer(Modifier.height(12.dp))
                Text("Tap mic to pause · Speak naturally · AI adapts",
                    fontSize = 12.sp, color = VoxlyColors.TextTertiary, textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun ControlButton(
    icon: String, label: String,
    tint: androidx.compose.ui.graphics.Color = VoxlyColors.SurfaceElevated,
    onClick: () -> Unit,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.size(52.dp).clip(CircleShape).background(tint)
            .clickable(onClick = onClick), contentAlignment = Alignment.Center) {
            Text(icon, fontSize = 22.sp)
        }
        Spacer(Modifier.height(4.dp))
        Text(label, fontSize = 11.sp, color = VoxlyColors.TextTertiary)
    }
}

private fun formatTime(seconds: Int) =
    "${(seconds / 60).toString().padStart(2, '0')}:${(seconds % 60).toString().padStart(2, '0')}"
