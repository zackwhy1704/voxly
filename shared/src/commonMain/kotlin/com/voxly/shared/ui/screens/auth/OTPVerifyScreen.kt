package com.voxly.shared.ui.screens.auth

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.voxly.shared.ui.components.OTPInputRow
import com.voxly.shared.ui.components.VoxlyButton
import com.voxly.shared.ui.screens.home.HomeScreen
import com.voxly.shared.ui.theme.VoxlyColors
import kotlinx.coroutines.delay

class OTPVerifyScreen(private val email: String) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var code by remember { mutableStateOf("") }
        var countdown by remember { mutableIntStateOf(42) }

        LaunchedEffect(Unit) {
            while (countdown > 0) { delay(1000); countdown-- }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(VoxlyColors.Background)
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Text("←", fontSize = 20.sp, color = VoxlyColors.TextPrimary,
                    modifier = Modifier.clickable { navigator.pop() })
                Spacer(Modifier.width(12.dp))
                StepProgressBar(currentStep = 3, totalSteps = 3, modifier = Modifier.weight(1f))
                Spacer(Modifier.width(12.dp))
                Text("3 of 3", fontSize = 12.sp, color = VoxlyColors.TextTertiary)
            }

            Spacer(Modifier.height(40.dp))

            // Email illustration with teal glow
            Box(
                modifier = Modifier.size(100.dp),
                contentAlignment = Alignment.Center,
            ) {
                Box(modifier = Modifier.size(100.dp).clip(CircleShape)
                    .background(VoxlyColors.Teal.copy(alpha = 0.08f)))
                Box(modifier = Modifier.size(60.dp).clip(CircleShape)
                    .background(VoxlyColors.Teal.copy(alpha = 0.15f)))
                Text("✉️", fontSize = 32.sp)
            }

            Spacer(Modifier.height(24.dp))

            Text("Check your email", fontSize = 26.sp, fontWeight = FontWeight.Bold,
                color = VoxlyColors.TextPrimary, textAlign = TextAlign.Center)
            Spacer(Modifier.height(8.dp))
            Text(
                text = "We sent a 6-digit code to\n${maskEmail(email)}",
                fontSize = 14.sp, color = VoxlyColors.TextSecondary, textAlign = TextAlign.Center,
                lineHeight = 22.sp,
            )

            Spacer(Modifier.height(32.dp))

            OTPInputRow(code = code, onCodeChange = { code = it })

            Spacer(Modifier.height(16.dp))

            if (countdown > 0) {
                Text(
                    text = "Resend code in 00:${countdown.toString().padStart(2, '0')}",
                    fontSize = 13.sp, color = VoxlyColors.TextTertiary,
                )
            } else {
                Text("Resend code", fontSize = 13.sp, color = VoxlyColors.Teal,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { countdown = 42 })
            }

            Spacer(Modifier.height(16.dp))

            // Tip banner
            Box(
                modifier = Modifier.fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(VoxlyColors.Teal08)
                    .padding(12.dp),
            ) {
                Text("💡 Tip: code will auto-fill if SMS is enabled",
                    fontSize = 12.sp, color = VoxlyColors.Teal)
            }

            Spacer(Modifier.weight(1f))

            VoxlyButton(
                label = "Verify & continue",
                onClick = { navigator.replaceAll(HomeScreen()) },
                isEnabled = code.length == 6,
            )

            Spacer(Modifier.height(16.dp))

            Text("Use phone number instead", fontSize = 13.sp, color = VoxlyColors.Teal,
                modifier = Modifier.clickable { })

            Spacer(Modifier.height(32.dp))
        }
    }
}

private fun maskEmail(email: String): String {
    val at = email.indexOf('@')
    if (at < 2) return email
    return email.take(2) + "***" + email.drop(at - 1)
}
