package com.voxly.shared.ui.screens.home

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.voxly.shared.domain.model.DailyChallenge
import com.voxly.shared.domain.model.Scenario
import com.voxly.shared.domain.model.ScoreBreakdown
import com.voxly.shared.domain.model.User
import com.voxly.shared.domain.usecase.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = true,
    val user: User? = null,
    val todaysMission: Scenario? = null,
    val recommendedScenarios: List<Scenario> = emptyList(),
    val weeklyXP: Int = 0,
    val careerScore: Int = 0,
    val skillScores: ScoreBreakdown = ScoreBreakdown(72, 55, 80, 63),
    val error: String? = null,
)

class HomeViewModel(
    private val getCurrentUser: GetCurrentUserFlowUseCase,
    private val getRecommended: GetRecommendedScenariosUseCase,
) : ScreenModel {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        observeUser()
    }

    private fun observeUser() {
        screenModelScope.launch {
            getCurrentUser().collect { user ->
                _uiState.update { it.copy(isLoading = false, user = user,
                    weeklyXP = user?.weeklyXP ?: 0,
                    careerScore = user?.careerReadinessScore ?: 76) }
                user?.id?.let { loadRecommended(it) }
            }
        }
    }

    private fun loadRecommended(userId: String) {
        screenModelScope.launch {
            getRecommended(userId)
                .onSuccess { scenarios ->
                    _uiState.update { it.copy(
                        todaysMission = scenarios.firstOrNull(),
                        recommendedScenarios = scenarios.drop(1).take(2),
                    )}
                }
                .onFailure { e -> _uiState.update { it.copy(error = e.message) } }
        }
    }
}
