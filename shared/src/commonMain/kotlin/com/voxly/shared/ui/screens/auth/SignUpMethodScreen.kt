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
import com.voxly.shared.ui.components.VoxlyButton
import com.voxly.shared.ui.components.VoxlyButtonVariant
import com.voxly.shared.ui.components.VoxlyInputField
import com.voxly.shared.ui.theme.VoxlyColors

class SignUpMethodScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var email by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(VoxlyColors.Background)
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 32.dp),
        ) {
            Spacer(Modifier.height(24.dp))

            // Back + progress
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "←",
                    fontSize = 20.sp,
                    color = VoxlyColors.TextPrimary,
                    modifier = Modifier.clickable { navigator.pop() },
                )
                Spacer(Modifier.width(12.dp))
                // Step progress — 3 segments
                StepProgressBar(currentStep = 1, totalSteps = 3, modifier = Modifier.weight(1f))
                Spacer(Modifier.width(12.dp))
                Text("1 of 3", fontSize = 12.sp, color = VoxlyColors.TextTertiary)
            }

            Spacer(Modifier.height(32.dp))

            Text(
                text = "Create your\naccount",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = VoxlyColors.TextPrimary,
                lineHeight = 38.sp,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Join 50,000+ SEA professionals",
                fontSize = 14.sp,
                color = VoxlyColors.TextSecondary,
            )

            Spacer(Modifier.height(32.dp))

            VoxlyButton(
                label = "G   Continue with Google",
                onClick = { /* Google OAuth */ },
                variant = VoxlyButtonVariant.Ghost,
            )
            Spacer(Modifier.height(12.dp))
            VoxlyButton(
                label = "   Continue with Apple",
                onClick = { /* Apple OAuth */ },
                variant = VoxlyButtonVariant.Ghost,
            )

            Spacer(Modifier.height(24.dp))

            // OR divider
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.weight(1f).height(1.dp).background(VoxlyColors.White12))
                Text("  or  ", fontSize = 12.sp, color = VoxlyColors.TextTertiary)
                Box(Modifier.weight(1f).height(1.dp).background(VoxlyColors.White12))
            }

            Spacer(Modifier.height(24.dp))

            VoxlyInputField(
                value = email,
                onValueChange = { email = it },
                label = "Email address",
                placeholder = "hello@example.com",
            )

            Spacer(Modifier.height(8.dp))
            Text(
                text = "or use phone number instead",
                fontSize = 13.sp,
                color = VoxlyColors.Teal,
                modifier = Modifier.clickable { },
            )

            Spacer(Modifier.height(24.dp))

            VoxlyButton(
                label = "Continue with email",
                onClick = { if (email.isNotBlank()) navigator.push(SignUpDetailsScreen(email)) },
                isEnabled = email.isNotBlank(),
            )

            Spacer(Modifier.weight(1f))

            Text(
                text = "By continuing you agree to our Terms and Privacy Policy",
                fontSize = 12.sp,
                color = VoxlyColors.TextTertiary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = buildAnnotatedString {
                    append("Already have an account? ")
                    withStyle(SpanStyle(color = VoxlyColors.Coral, fontWeight = FontWeight.SemiBold)) {
                        append("Log in")
                    }
                },
                fontSize = 14.sp,
                color = VoxlyColors.TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navigator.push(LoginScreen()) },
            )
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
fun StepProgressBar(currentStep: Int, totalSteps: Int, modifier: Modifier = Modifier) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        repeat(totalSteps) { index ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        if (index < currentStep) VoxlyColors.Coral else VoxlyColors.White12,
                    ),
            )
        }
    }
}
