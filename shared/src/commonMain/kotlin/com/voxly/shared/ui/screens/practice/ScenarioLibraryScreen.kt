package com.voxly.shared.ui.screens.practice

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.voxly.shared.domain.model.ScenarioCategory
import com.voxly.shared.ui.components.*
import com.voxly.shared.ui.theme.VoxlyColors

class ScenarioLibraryScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<ScenarioLibraryViewModel>()
        val state by viewModel.uiState.collectAsState()

        Box(modifier = Modifier.fillMaxSize().background(VoxlyColors.Background)) {
            Column(
                modifier = Modifier.fillMaxSize().padding(bottom = 68.dp),
            ) {
                // Header
                Box(
                    modifier = Modifier.fillMaxWidth().background(VoxlyColors.Surface)
                        .statusBarsPadding().padding(horizontal = 24.dp, vertical = 16.dp),
                ) {
                    Column {
                        Text("Scenarios", fontSize = 26.sp, fontWeight = FontWeight.Bold,
                            color = VoxlyColors.TextPrimary)
                        Text("Pick your battlefield", fontSize = 14.sp, color = VoxlyColors.TextSecondary)
                    }
                }

                Spacer(Modifier.height(12.dp))

                // Search bar
                Box(
                    modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp)).background(VoxlyColors.SurfaceElevated)
                        .padding(horizontal = 16.dp, vertical = 13.dp),
                ) {
                    if (state.searchQuery.isEmpty()) {
                        Text("🔍  Search scenarios", fontSize = 14.sp,
                            color = VoxlyColors.TextTertiary)
                    }
                    // In a real impl: BasicTextField here
                }

                Spacer(Modifier.height(12.dp))

                // Category filter pills
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    item {
                        FilterPill("All", state.selectedCategory == null) {
                            viewModel.onAction(ScenarioLibraryAction.FilterCategory(null))
                        }
                    }
                    items(ScenarioCategory.entries) { cat ->
                        FilterPill(cat.displayName, state.selectedCategory == cat) {
                            viewModel.onAction(ScenarioLibraryAction.FilterCategory(cat))
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                // Scenario list
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    items(state.filteredScenarios) { scenario ->
                        ScenarioCard(
                            scenario = scenario,
                            onTap = { navigator.push(SessionBriefScreen(scenario.id)) },
                        )
                    }
                }
            }

            VoxlyBottomNav(
                activeTab = VoxlyTab.PRACTICE,
                onTabSelected = { if (it == VoxlyTab.HOME) navigator.pop() },
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}

@Composable
private fun FilterPill(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) VoxlyColors.Coral else VoxlyColors.SurfaceElevated)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 7.dp),
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isSelected) androidx.compose.ui.graphics.Color.White
                    else VoxlyColors.TextSecondary,
        )
    }
}
