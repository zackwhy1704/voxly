package com.voxly.shared.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import com.voxly.shared.ui.components.*
import com.voxly.shared.ui.theme.VoxlyColors

class ForgotPasswordScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var email by remember { mutableStateOf("") }
        var sent by remember { mutableStateOf(false) }

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
            Row(modifier = Modifier.fillMaxWidth()) {
                Text("←", fontSize = 20.sp, color = VoxlyColors.TextPrimary,
                    modifier = Modifier.clickable { navigator.pop() })
            }

            Spacer(Modifier.height(40.dp))

            Box(modifier = Modifier.size(100.dp), contentAlignment = Alignment.Center) {
                Box(modifier = Modifier.size(100.dp).clip(CircleShape)
                    .background(VoxlyColors.Coral.copy(alpha = 0.1f)))
                Box(modifier = Modifier.size(60.dp).clip(CircleShape)
                    .background(VoxlyColors.Coral.copy(alpha = 0.2f)))
                Text("🔐", fontSize = 32.sp)
            }

            Spacer(Modifier.height(24.dp))

            Text("Reset your password", fontSize = 26.sp, fontWeight = FontWeight.Bold,
                color = VoxlyColors.TextPrimary, textAlign = TextAlign.Center)
            Spacer(Modifier.height(8.dp))
            Text(
                "Enter your email and we'll send you a link to reset your password.",
                fontSize = 14.sp, color = VoxlyColors.TextSecondary, textAlign = TextAlign.Center,
                lineHeight = 22.sp,
            )

            Spacer(Modifier.height(32.dp))

            if (!sent) {
                VoxlyInputField(value = email, onValueChange = { email = it },
                    label = "Email address", placeholder = "hello@example.com")

                Spacer(Modifier.height(24.dp))

                VoxlyButton("Send reset link", onClick = { sent = true },
                    isEnabled = email.isNotBlank())

                Spacer(Modifier.height(24.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.weight(1f).height(1.dp).background(VoxlyColors.White12))
                    Text("  or  ", fontSize = 12.sp, color = VoxlyColors.TextTertiary)
                    Box(Modifier.weight(1f).height(1.dp).background(VoxlyColors.White12))
                }

                Spacer(Modifier.height(16.dp))

                VoxlyButton("Reset via SMS", onClick = {}, variant = VoxlyButtonVariant.Secondary)
            } else {
                Text("✅ Reset link sent to\n$email",
                    fontSize = 16.sp, color = VoxlyColors.Green, textAlign = TextAlign.Center,
                    lineHeight = 24.sp)
                Spacer(Modifier.height(24.dp))
                VoxlyButton("Back to login", onClick = { navigator.pop() },
                    variant = VoxlyButtonVariant.Ghost)
            }
        }
    }
}
