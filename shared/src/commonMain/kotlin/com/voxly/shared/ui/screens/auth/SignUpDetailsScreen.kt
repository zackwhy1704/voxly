package com.voxly.shared.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.voxly.shared.domain.model.LearningGoal
import com.voxly.shared.ui.components.*
import com.voxly.shared.ui.theme.VoxlyColors

class SignUpDetailsScreen(private val email: String) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var name by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var country by remember { mutableStateOf("Singapore") }
        var selectedGoal by remember { mutableStateOf<LearningGoal?>(null) }

        val passwordStrength = when {
            password.length < 6 -> 0f
            password.length < 10 -> 0.5f
            else -> 1f
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(VoxlyColors.Background)
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 32.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("←", fontSize = 20.sp, color = VoxlyColors.TextPrimary,
                    modifier = Modifier.clickable { navigator.pop() })
                Spacer(Modifier.width(12.dp))
                StepProgressBar(currentStep = 2, totalSteps = 3, modifier = Modifier.weight(1f))
                Spacer(Modifier.width(12.dp))
                Text("2 of 3", fontSize = 12.sp, color = VoxlyColors.TextTertiary)
            }

            Spacer(Modifier.height(32.dp))

            Text("Tell us about yourself",
                fontSize = 28.sp, fontWeight = FontWeight.Bold, color = VoxlyColors.TextPrimary)

            Spacer(Modifier.height(24.dp))

            VoxlyInputField(value = name, onValueChange = { name = it },
                label = "Full name", placeholder = "Sarah Tan")

            Spacer(Modifier.height(16.dp))

            VoxlyInputField(value = password, onValueChange = { password = it },
                label = "Password", placeholder = "••••••••••", isPassword = true)

            if (password.isNotEmpty()) {
                Spacer(Modifier.height(6.dp))
                val (barColor, label) = when {
                    passwordStrength < 0.4f -> Pair(VoxlyColors.Coral, "Weak")
                    passwordStrength < 0.8f -> Pair(VoxlyColors.Gold, "Medium")
                    else                    -> Pair(VoxlyColors.Green, "Strong")
                }
                VoxlyProgressBar(progress = passwordStrength, color = barColor,
                    modifier = Modifier.fillMaxWidth(), height = 4)
                Text(label, fontSize = 11.sp, color = barColor,
                    modifier = Modifier.align(Alignment.End))
            }

            Spacer(Modifier.height(16.dp))

            // Country picker (simplified)
            VoxlyInputField(value = country, onValueChange = { country = it },
                label = "Country", placeholder = "Singapore")

            Spacer(Modifier.height(24.dp))

            Text("Your goal", fontSize = 14.sp, fontWeight = FontWeight.SemiBold,
                color = VoxlyColors.TextSecondary)
            Spacer(Modifier.height(8.dp))

            LearningGoal.entries.forEach { goal ->
                GoalOption(goal = goal, isSelected = selectedGoal == goal,
                    onSelect = { selectedGoal = goal })
                Spacer(Modifier.height(8.dp))
            }

            Spacer(Modifier.height(24.dp))

            VoxlyButton(
                label = "Continue",
                onClick = { navigator.push(OTPVerifyScreen(email)) },
                isEnabled = name.isNotBlank() && password.length >= 6 && selectedGoal != null,
            )

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun GoalOption(goal: LearningGoal, isSelected: Boolean, onSelect: () -> Unit) {
    val shape = RoundedCornerShape(12.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(if (isSelected) VoxlyColors.Coral20 else VoxlyColors.SurfaceElevated)
            .border(1.dp,
                if (isSelected) VoxlyColors.Coral.copy(alpha = 0.6f) else VoxlyColors.White12,
                shape)
            .clickable(onClick = onSelect)
            .padding(horizontal = 16.dp, vertical = 14.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(goal.displayName, fontSize = 14.sp, color = VoxlyColors.TextPrimary)
            if (isSelected) Text("✓", fontSize = 14.sp, color = VoxlyColors.Coral)
        }
    }
}
