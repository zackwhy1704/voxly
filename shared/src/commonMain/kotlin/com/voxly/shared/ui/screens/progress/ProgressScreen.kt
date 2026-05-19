package com.voxly.shared.ui.screens.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.voxly.shared.domain.model.ScoreBreakdown
import com.voxly.shared.ui.components.*
import com.voxly.shared.ui.theme.VoxlyColors

class ProgressScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Box(modifier = Modifier.fillMaxSize().background(VoxlyColors.Background)) {
            Column(modifier = Modifier.fillMaxSize().padding(bottom = 68.dp)
                .verticalScroll(rememberScrollState())) {

                // Header with profile
                Box(modifier = Modifier.fillMaxWidth().background(VoxlyColors.Surface)
                    .statusBarsPadding().padding(horizontal = 20.dp, vertical = 16.dp)) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            // Avatar
                            Box(modifier = Modifier.size(52.dp).clip(CircleShape)
                                .background(VoxlyColors.Coral), contentAlignment = Alignment.Center) {
                                Text("ST", fontSize = 18.sp, fontWeight = FontWeight.Bold,
                                    color = androidx.compose.ui.graphics.Color.White)
                            }
                            Column {
                                Text("Sarah Tan", fontSize = 17.sp, fontWeight = FontWeight.SemiBold,
                                    color = VoxlyColors.TextPrimary)
                                Text("Senior Manager · Singapore", fontSize = 12.sp,
                                    color = VoxlyColors.TextSecondary)
                            }
                        }
                        // Level badge
                        Box(modifier = Modifier.clip(RoundedCornerShape(20.dp))
                            .background(VoxlyColors.Gold10).padding(horizontal = 12.dp, vertical = 6.dp)) {
                            Text("⭐ Level 8", fontSize = 12.sp, color = VoxlyColors.Gold,
                                fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Stats strip
                Row(modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Sessions" to "23", "Time" to "5.2h", "Streak 🔥" to "13", "Best" to "84").forEach { (label, value) ->
                        Column(modifier = Modifier.weight(1f).clip(RoundedCornerShape(12.dp))
                            .background(VoxlyColors.SurfaceElevated).padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold,
                                color = VoxlyColors.TextPrimary)
                            Text(label, fontSize = 10.sp, color = VoxlyColors.TextTertiary)
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Career readiness card
                CareerReadinessCard(score = 76, tier = "Silver II", rank = "Senior Manager · Top 18%",
                    deltaThisMonth = 8, modifier = Modifier.padding(horizontal = 20.dp))

                Spacer(Modifier.height(16.dp))

                // Score breakdown
                Box(modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)).background(VoxlyColors.SurfaceElevated)
                    .padding(16.dp)) {
                    Column {
                        Text("Skill Breakdown", fontSize = 14.sp, fontWeight = FontWeight.SemiBold,
                            color = VoxlyColors.TextPrimary)
                        Spacer(Modifier.height(12.dp))
                        ProgressBar4D(scores = ScoreBreakdown(72, 55, 80, 63))
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Recent sessions
                Row(modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Recent sessions", fontSize = 16.sp, fontWeight = FontWeight.SemiBold,
                        color = VoxlyColors.TextPrimary)
                }
                Spacer(Modifier.height(8.dp))
                listOf(
                    Triple("Tech Interview Prep", "Score: 76 · +8 improvement", VoxlyColors.Coral),
                    Triple("Board Presentation", "Score: 84 · Personal best", VoxlyColors.Purple),
                    Triple("Client Discovery", "Score: 68 · +3 improvement", VoxlyColors.Teal),
                ).forEach { (title, sub, color) ->
                    Box(modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp)).background(VoxlyColors.SurfaceElevated)) {
                        Box(modifier = Modifier.width(4.dp).fillMaxHeight().background(color))
                        Row(modifier = Modifier.fillMaxWidth()
                            .padding(start = 16.dp, top = 12.dp, bottom = 12.dp, end = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold,
                                    color = VoxlyColors.TextPrimary)
                                Text(sub, fontSize = 12.sp, color = VoxlyColors.TextSecondary)
                            }
                            Text("View", fontSize = 12.sp, color = VoxlyColors.Coral,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.clickable { })
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                }

                Spacer(Modifier.height(8.dp))

                // SkillsFuture banner
                Box(modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(VoxlyColors.Green.copy(alpha = 0.08f)).padding(16.dp)) {
                    Text("🇸🇬 SkillsFuture eligible · S\$500 credit applicable",
                        fontSize = 13.sp, color = VoxlyColors.Green, fontWeight = FontWeight.SemiBold)
                }

                Spacer(Modifier.height(16.dp))

                // Achievements
                Row(modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("🏆", "🔥", "⚡", "🎯", "📚").forEachIndexed { idx, icon ->
                        Box(modifier = Modifier.size(48.dp).clip(CircleShape)
                            .background(if (idx < 3) VoxlyColors.Gold10 else VoxlyColors.SurfaceElevated),
                            contentAlignment = Alignment.Center) {
                            Text(icon, fontSize = 22.sp,
                                color = if (idx < 3) androidx.compose.ui.graphics.Color.Unspecified
                                        else VoxlyColors.TextTertiary)
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))
            }

            VoxlyBottomNav(activeTab = VoxlyTab.PROGRESS,
                onTabSelected = { if (it == VoxlyTab.HOME) navigator.pop() },
                modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
}
