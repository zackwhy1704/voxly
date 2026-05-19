package com.voxly.shared.ui.screens.learn

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.voxly.shared.domain.model.*
import com.voxly.shared.ui.components.*
import com.voxly.shared.ui.theme.VoxlyColors

class LearnScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<LearnViewModel>()
        val state by viewModel.uiState.collectAsState()

        Box(modifier = Modifier.fillMaxSize().background(VoxlyColors.Background)) {
            Column(modifier = Modifier.fillMaxSize().padding(bottom = 68.dp)) {
                // Header
                Box(modifier = Modifier.fillMaxWidth().background(VoxlyColors.Surface)
                    .statusBarsPadding().padding(horizontal = 24.dp, vertical = 16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically) {
                        Text("Your path", fontSize = 22.sp, fontWeight = FontWeight.Bold,
                            color = VoxlyColors.TextPrimary)
                        Box(modifier = Modifier.clip(RoundedCornerShape(20.dp))
                            .background(VoxlyColors.Gold10).padding(horizontal = 12.dp, vertical = 6.dp)) {
                            Text("⚡ ${state.totalXP} XP", fontSize = 13.sp, color = VoxlyColors.Gold,
                                fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                LazyColumn(
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(state.units) { unit ->
                        LearningUnitCard(unit = unit)
                    }

                    // Bottom CTA
                    item {
                        if (state.currentLesson != null) {
                            Box(modifier = Modifier.fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(VoxlyColors.Coral).padding(16.dp),
                                contentAlignment = Alignment.Center) {
                                Text("▶ Continue: ${state.currentLesson}",
                                    fontSize = 15.sp, fontWeight = FontWeight.SemiBold,
                                    color = Color.White)
                            }
                        }
                    }
                }
            }

            VoxlyBottomNav(
                activeTab = VoxlyTab.LEARN,
                onTabSelected = { tab ->
                    if (tab == VoxlyTab.HOME) navigator.pop()
                },
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}

@Composable
private fun LearningUnitCard(unit: LearningUnit) {
    val color = Color(unit.colorHex)
    val alpha = if (unit.isLocked) 0.4f else 1f

    Column {
        // Unit header banner
        Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.15f * alpha))
            .padding(horizontal = 16.dp, vertical = 10.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(unit.title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold,
                    color = color.copy(alpha = alpha))
                if (unit.isLocked) Text("🔒", fontSize = 14.sp)
            }
        }

        Spacer(Modifier.height(10.dp))

        // 3 nodes in a row with connectors
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically) {
            unit.nodes.forEachIndexed { idx, node ->
                LearningPathNode(
                    nodeNumber = node.position,
                    state = node.state,
                    accentColor = color,
                    onTap = { },
                    size = 60,
                )
                if (idx < unit.nodes.size - 1) {
                    Box(modifier = Modifier.weight(1f).height(3.dp)
                        .background(color.copy(alpha = 0.3f * alpha)))
                }
            }
        }
    }
}
