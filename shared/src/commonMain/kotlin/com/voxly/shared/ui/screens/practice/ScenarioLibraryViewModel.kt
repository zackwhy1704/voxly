package com.voxly.shared.ui.screens.practice

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.voxly.shared.domain.model.DifficultyTier
import com.voxly.shared.domain.model.Scenario
import com.voxly.shared.domain.model.ScenarioCategory
import com.voxly.shared.domain.usecase.GetScenariosUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ScenarioLibraryUiState(
    val isLoading: Boolean = true,
    val scenarios: List<Scenario> = emptyList(),
    val selectedCategory: ScenarioCategory? = null,
    val searchQuery: String = "",
    val error: String? = null,
) {
    val filteredScenarios: List<Scenario>
        get() = scenarios.filter { s ->
            (selectedCategory == null || s.category == selectedCategory) &&
                (searchQuery.isBlank() || s.title.contains(searchQuery, ignoreCase = true))
        }
}

sealed interface ScenarioLibraryAction {
    data class FilterCategory(val category: ScenarioCategory?) : ScenarioLibraryAction
    data class Search(val query: String) : ScenarioLibraryAction
}

class ScenarioLibraryViewModel(
    private val getScenarios: GetScenariosUseCase,
) : ScreenModel {

    private val _uiState = MutableStateFlow(ScenarioLibraryUiState())
    val uiState: StateFlow<ScenarioLibraryUiState> = _uiState.asStateFlow()

    init { load() }

    private fun load() {
        screenModelScope.launch {
            getScenarios()
                .onSuccess { list -> _uiState.update { it.copy(isLoading = false, scenarios = list) } }
                .onFailure { e -> _uiState.update { it.copy(isLoading = false, error = e.message,
                    scenarios = sampleScenarios()) } }
        }
    }

    fun onAction(action: ScenarioLibraryAction) {
        when (action) {
            is ScenarioLibraryAction.FilterCategory -> _uiState.update { it.copy(selectedCategory = action.category) }
            is ScenarioLibraryAction.Search -> _uiState.update { it.copy(searchQuery = action.query) }
        }
    }
}

private fun sampleScenarios() = listOf(
    Scenario("1", "Tech Company Interview", "Software Eng · 20 min · Hard",
        DifficultyTier.GOLD, 20, ScenarioCategory.INTERVIEW,
        "Senior engineering interview at a top tech firm.", 0xFFCC8833, 45, progressFraction = 0.3f),
    Scenario("2", "Board Presentation", "C-Suite · 15 min · Hard",
        DifficultyTier.PLATINUM, 15, ScenarioCategory.PRESENTATION,
        "Present Q3 results to the board.", 0xFF80CCDD, 60, isLocked = false),
    Scenario("3", "Client Discovery Call", "Consulting · 10 min · Med",
        DifficultyTier.SILVER, 10, ScenarioCategory.MEETING,
        "Uncover client needs in an initial discovery call.", 0xFFAAAAAA, 30, progressFraction = 0.7f),
    Scenario("4", "Performance Review", "HR · 12 min · Easy",
        DifficultyTier.BRONZE, 12, ScenarioCategory.MEETING,
        "Navigate your annual performance review discussion.", 0xFFCC8833, 20, progressFraction = 1f),
)
