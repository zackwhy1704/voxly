package com.voxly.shared.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.voxly.shared.ui.components.VoxlyButton
import com.voxly.shared.ui.components.VoxlyButtonVariant
import com.voxly.shared.ui.theme.VoxlyColors

class LandingScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(VoxlyColors.Background),
        ) {
            // Ambient glow
            Box(
                modifier = Modifier
                    .size(320.dp)
                    .align(Alignment.TopCenter)
                    .offset(y = 120.dp)
                    .clip(CircleShape)
                    .background(VoxlyColors.Coral.copy(alpha = 0.06f)),
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp)
                    .statusBarsPadding()
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(Modifier.weight(1f))

                // Logo
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(VoxlyColors.Coral),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("V", fontSize = 38.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                Spacer(Modifier.height(24.dp))

                Text(
                    text = "voxly",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = VoxlyColors.TextPrimary,
                )
                Text(
                    text = "Speak with confidence",
                    fontSize = 16.sp,
                    color = VoxlyColors.TextPrimary.copy(alpha = 0.45f),
                )

                Spacer(Modifier.weight(1f))

                // CTAs
                VoxlyButton(
                    label = "Get started — it's free",
                    onClick = { navigator.push(SignUpMethodScreen()) },
                    variant = VoxlyButtonVariant.Primary,
                )

                Spacer(Modifier.height(12.dp))

                VoxlyButton(
                    label = "Continue with Google",
                    onClick = { /* Google OAuth */ },
                    variant = VoxlyButtonVariant.Ghost,
                )

                Spacer(Modifier.height(24.dp))

                Text(
                    text = buildAnnotatedString {
                        append("Already have an account? ")
                        withStyle(SpanStyle(color = VoxlyColors.Coral, fontWeight = FontWeight.SemiBold)) {
                            append("Log in")
                        }
                    },
                    fontSize = 14.sp,
                    color = VoxlyColors.TextSecondary,
                    modifier = Modifier.clickable { navigator.push(LoginScreen()) },
                )

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}
