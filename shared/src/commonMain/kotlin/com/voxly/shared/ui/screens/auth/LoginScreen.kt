package com.voxly.shared.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.voxly.shared.ui.components.*
import com.voxly.shared.ui.screens.home.HomeScreen
import com.voxly.shared.ui.theme.VoxlyColors

class LoginScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(VoxlyColors.Background)
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 32.dp),
        ) {
            Spacer(Modifier.height(48.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(46.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(VoxlyColors.Coral),
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Welcome back", fontSize = 28.sp, fontWeight = FontWeight.Bold,
                        color = VoxlyColors.TextPrimary)
                    Text("Continue where you left off", fontSize = 14.sp,
                        color = VoxlyColors.TextSecondary)
                }
            }

            Spacer(Modifier.height(40.dp))

            VoxlyButton("G   Continue with Google", {}, variant = VoxlyButtonVariant.Ghost)
            Spacer(Modifier.height(12.dp))
            VoxlyButton("   Continue with Apple", {}, variant = VoxlyButtonVariant.Ghost)

            Spacer(Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.weight(1f).height(1.dp).background(VoxlyColors.White12))
                Text("  or  ", fontSize = 12.sp, color = VoxlyColors.TextTertiary)
                Box(Modifier.weight(1f).height(1.dp).background(VoxlyColors.White12))
            }

            Spacer(Modifier.height(24.dp))

            VoxlyInputField(value = email, onValueChange = { email = it },
                label = "Email or phone", placeholder = "sarah@company.com")
            Spacer(Modifier.height(16.dp))
            VoxlyInputField(value = password, onValueChange = { password = it },
                label = "Password", placeholder = "••••••••••", isPassword = true)

            Spacer(Modifier.height(8.dp))
            Text("Forgot password?", fontSize = 13.sp, color = VoxlyColors.Coral,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.End).clickable {
                    navigator.push(ForgotPasswordScreen())
                })

            Spacer(Modifier.height(24.dp))

            VoxlyButton(
                label = "Log in",
                onClick = { navigator.replaceAll(HomeScreen()) },
                isEnabled = email.isNotBlank() && password.isNotBlank(),
            )

            Spacer(Modifier.weight(1f))

            Text(
                text = buildAnnotatedString {
                    append("New to Voxly? ")
                    withStyle(SpanStyle(color = VoxlyColors.Coral, fontWeight = FontWeight.SemiBold)) {
                        append("Create a free account")
                    }
                },
                fontSize = 14.sp,
                color = VoxlyColors.TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().clickable { navigator.push(SignUpMethodScreen()) },
            )

            Spacer(Modifier.height(32.dp))
        }
    }
}
