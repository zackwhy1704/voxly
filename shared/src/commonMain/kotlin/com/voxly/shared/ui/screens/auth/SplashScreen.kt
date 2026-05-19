package com.voxly.shared.ui.screens.auth

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.voxly.shared.ui.screens.home.HomeScreen
import com.voxly.shared.ui.theme.VoxlyColors
import kotlinx.coroutines.delay

class SplashScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val infiniteTransition = rememberInfiniteTransition(label = "splash")
        val glow by infiniteTransition.animateFloat(
            initialValue = 0.8f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(tween(2000), RepeatMode.Reverse),
            label = "glow",
        )

        LaunchedEffect(Unit) {
            delay(1500)
            navigator.replace(LandingScreen())
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(VoxlyColors.Background),
            contentAlignment = Alignment.Center,
        ) {
            // Concentric glow circles
            Box(
                modifier = Modifier
                    .size(320.dp)
                    .scale(glow)
                    .clip(CircleShape)
                    .background(VoxlyColors.Coral.copy(alpha = 0.06f)),
            )
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .scale(glow)
                    .clip(CircleShape)
                    .background(VoxlyColors.Coral.copy(alpha = 0.09f)),
            )
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .scale(glow)
                    .clip(CircleShape)
                    .background(VoxlyColors.Coral.copy(alpha = 0.15f)),
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Logo circle
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(VoxlyColors.Coral),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "V",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "voxly",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = VoxlyColors.TextPrimary,
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Speak with confidence",
                    fontSize = 16.sp,
                    color = VoxlyColors.TextPrimary.copy(alpha = 0.45f),
                )
            }
        }
    }
}
