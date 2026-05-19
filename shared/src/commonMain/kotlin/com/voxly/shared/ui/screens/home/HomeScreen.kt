package com.voxly.shared.ui.screens.home

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.voxly.shared.ui.components.*
import com.voxly.shared.ui.screens.practice.ScenarioLibraryScreen
import com.voxly.shared.ui.screens.practice.SessionBriefScreen
import com.voxly.shared.ui.theme.VoxlyColors

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<HomeViewModel>()
        val state by viewModel.uiState.collectAsState()

        var activeTab by remember { mutableStateOf(VoxlyTab.HOME) }

        Box(modifier = Modifier.fillMaxSize().background(VoxlyColors.Background)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 68.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(VoxlyColors.Surface)
                        .statusBarsPadding()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                ) {
                    Column(modifier = Modifier.align(Alignment.CenterStart)) {
                        Text("Good morning,", fontSize = 14.sp, color = VoxlyColors.TextSecondary)
                        Text(
                            state.user?.name?.split(" ")?.firstOrNull() ?: "Sarah",
                            fontSize = 26.sp, fontWeight = FontWeight.Bold,
                            color = VoxlyColors.TextPrimary,
                        )
                    }
                    Row(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Box(
                            modifier = Modifier.clip(RoundedCornerShape(20.dp))
                                .background(VoxlyColors.SurfaceElevated)
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                        ) {
                            Text("🔥 ${state.user?.streakDays ?: 12} day streak",
                                fontSize = 12.sp, color = VoxlyColors.Gold)
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Today's mission card
                state.todaysMission?.let { scenario ->
                    MissionCard(
                        title = scenario.title,
                        subtitle = scenario.subtitle,
                        progress = scenario.progressFraction,
                        onResume = { navigator.push(SessionBriefScreen(scenario.id)) },
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                } ?: MissionCard(
                    title = "Job Interview Prep",
                    subtitle = "MNC Tech Company · 15 min",
                    progress = 0.35f,
                    onResume = {},
                    modifier = Modifier.padding(horizontal = 20.dp),
                )

                Spacer(Modifier.height(20.dp))

                // Skills grid
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text("Your skills", fontSize = 17.sp, fontWeight = FontWeight.SemiBold,
                        color = VoxlyColors.TextPrimary)
                    Text("See all", fontSize = 14.sp, color = VoxlyColors.Teal,
                        modifier = Modifier.clickable { })
                }
                Spacer(Modifier.height(10.dp))
                SkillGrid(scores = state.skillScores, modifier = Modifier.padding(horizontal = 20.dp))

                Spacer(Modifier.height(20.dp))

                // Recommended
                Row(modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Recommended for you", fontSize = 17.sp, fontWeight = FontWeight.SemiBold,
                        color = VoxlyColors.TextPrimary)
                    Text("View all", fontSize = 14.sp, color = VoxlyColors.Teal,
                        modifier = Modifier.clickable { navigator.push(ScenarioLibraryScreen()) })
                }
                Spacer(Modifier.height(10.dp))

                state.recommendedScenarios.forEach { s ->
                    ScenarioCard(scenario = s, onTap = { navigator.push(SessionBriefScreen(s.id)) },
                        modifier = Modifier.padding(horizontal = 20.dp))
                    Spacer(Modifier.height(8.dp))
                }
                if (state.recommendedScenarios.isEmpty()) {
                    // Placeholder cards
                    SampleScenarioCards(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        onTap = { navigator.push(ScenarioLibraryScreen()) },
                    )
                }

                Spacer(Modifier.height(12.dp))

                // XP strip
                Box(
                    modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(VoxlyColors.Gold10)
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                ) {
                    Text(
                        "⚡ ${state.weeklyXP.takeIf { it > 0 } ?: 340} XP this week  " +
                            "Top 18% of users",
                        fontSize = 13.sp, color = VoxlyColors.Gold, fontWeight = FontWeight.SemiBold,
                    )
                }

                Spacer(Modifier.height(16.dp))
            }

            // Bottom nav
            VoxlyBottomNav(
                activeTab = activeTab,
                onTabSelected = { tab ->
                    activeTab = tab
                    when (tab) {
                        VoxlyTab.PRACTICE -> navigator.push(ScenarioLibraryScreen())
                        else -> {}
                    }
                },
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}

@Composable
private fun MissionCard(
    title: String,
    subtitle: String,
    progress: Float,
    onResume: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(VoxlyColors.SurfaceElevated),
    ) {
        Box(modifier = Modifier.width(4.dp).fillMaxHeight().background(VoxlyColors.Coral))
        Column(modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 16.dp, end = 16.dp)) {
            Text("Today's mission", fontSize = 12.sp, color = VoxlyColors.TextSecondary)
            Spacer(Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(title, fontSize = 17.sp, fontWeight = FontWeight.SemiBold,
                        color = VoxlyColors.TextPrimary)
                    Text(subtitle, fontSize = 12.sp, color = VoxlyColors.TextSecondary)
                }
                Spacer(Modifier.width(8.dp))
                VoxlyButton("Start", onClick = onResume,
                    modifier = Modifier.width(68.dp), height = 36)
            }
            Spacer(Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                VoxlyProgressBar(progress = progress, color = VoxlyColors.Coral,
                    modifier = Modifier.weight(1f))
                Spacer(Modifier.width(8.dp))
                Text("${(progress * 100).toInt()}%", fontSize = 12.sp, color = VoxlyColors.Coral,
                    fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun SkillGrid(scores: com.voxly.shared.domain.model.ScoreBreakdown, modifier: Modifier = Modifier) {
    val items = listOf(
        Triple("Pronunciation", scores.pronunciation, VoxlyColors.Teal),
        Triple("Fluency",       scores.fluency,       VoxlyColors.Coral),
        Triple("Vocabulary",    scores.vocabulary,    VoxlyColors.Gold),
        Triple("Grammar",       scores.grammar,       VoxlyColors.Green),
    )
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items.forEach { (label, score, color) ->
            Column(
                modifier = Modifier.weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(VoxlyColors.SurfaceElevated)
                    .padding(10.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text("$score", fontSize = 22.sp, fontWeight = FontWeight.Bold,
                        color = VoxlyColors.TextPrimary)
                    Text("%", fontSize = 11.sp, color = VoxlyColors.TextTertiary,
                        modifier = Modifier.padding(bottom = 2.dp))
                }
                Spacer(Modifier.height(4.dp))
                VoxlyProgressBar(progress = score / 100f, color = color,
                    modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(4.dp))
                Text(label, fontSize = 10.sp, color = VoxlyColors.TextTertiary)
            }
        }
    }
}

@Composable
private fun SampleScenarioCards(modifier: Modifier = Modifier, onTap: () -> Unit) {
    val sampleColor = Color(0xFFCC8833)
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf("Salary Negotiation" to "Finance · Hard",
               "Cold Email Pitch" to "Sales · Medium").forEach { (title, sub) ->
            Box(
                modifier = Modifier.fillMaxWidth().height(84.dp)
                    .clip(RoundedCornerShape(18.dp)).background(VoxlyColors.SurfaceElevated)
                    .clickable(onClick = onTap),
            ) {
                Box(Modifier.width(4.dp).fillMaxHeight().background(sampleColor))
                Row(Modifier.fillMaxSize().padding(start = 20.dp, end = 12.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold,
                            color = VoxlyColors.TextPrimary)
                        Text(sub, fontSize = 12.sp, color = VoxlyColors.TextSecondary)
                    }
                    VoxlyButton("Try", onClick = onTap,
                        modifier = Modifier.width(56.dp), height = 30,
                        variant = VoxlyButtonVariant.Secondary)
                }
            }
        }
    }
}
