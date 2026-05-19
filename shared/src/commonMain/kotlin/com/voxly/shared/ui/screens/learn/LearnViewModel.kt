package com.voxly.shared.ui.screens.learn

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.voxly.shared.domain.model.*
import com.voxly.shared.domain.usecase.GetLearningPathUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class LearnUiState(
    val isLoading: Boolean = true,
    val units: List<LearningUnit> = emptyList(),
    val currentLesson: String? = null,
    val totalXP: Int = 340,
    val error: String? = null,
)

class LearnViewModel(
    private val getLearningPath: GetLearningPathUseCase,
) : ScreenModel {

    private val _uiState = MutableStateFlow(LearnUiState())
    val uiState: StateFlow<LearnUiState> = _uiState.asStateFlow()

    init { load() }

    private fun load() {
        screenModelScope.launch {
            getLearningPath("current_user")
                .onSuccess { units ->
                    val inProgress = units.flatMap { it.nodes }.firstOrNull { it.state == NodeState.IN_PROGRESS }
                    _uiState.update { it.copy(isLoading = false, units = units,
                        currentLesson = inProgress?.title) }
                }
                .onFailure { _uiState.update { it.copy(isLoading = false, units = sampleUnits()) } }
        }
    }
}

private fun sampleUnits() = listOf(
    LearningUnit("u1", "Foundations", 0xFF3ADC88, listOf(
        LessonNode("l1", "u1", 1, "Greetings & small talk", LessonType.SPEAK_IT, NodeState.COMPLETED, 15),
        LessonNode("l2", "u1", 2, "Email phrases", LessonType.FILL_IN, NodeState.COMPLETED, 15),
        LessonNode("l3", "u1", 3, "Active listening", LessonType.TAP_WHAT_YOU_HEAR, NodeState.COMPLETED, 15),
    ), isLocked = false),
    LearningUnit("u2", "Professional Speech", 0xFF2DD4BF, listOf(
        LessonNode("l4", "u2", 1, "Meeting language", LessonType.SPEAK_IT, NodeState.COMPLETED, 20),
        LessonNode("l5", "u2", 2, "Disagreeing politely", LessonType.SPEAK_IT, NodeState.COMPLETED, 20),
        LessonNode("l6", "u2", 3, "Presentation openers", LessonType.PRONUNCIATION_DRILL, NodeState.COMPLETED, 20),
    ), isLocked = false),
    LearningUnit("u3", "Interview Mastery", 0xFFFF6B5B, listOf(
        LessonNode("l7", "u3", 1, "STAR method", LessonType.SPEAK_IT, NodeState.COMPLETED, 30),
        LessonNode("l8", "u3", 2, "Strengths & weaknesses", LessonType.SPEAK_IT, NodeState.IN_PROGRESS, 30),
        LessonNode("l9", "u3", 3, "Salary negotiation", LessonType.SPEAK_IT, NodeState.AVAILABLE, 30),
    ), isLocked = false),
    LearningUnit("u4", "Executive Presence", 0xFF9966E5, listOf(
        LessonNode("l10", "u4", 1, "Executive vocabulary", LessonType.FILL_IN, NodeState.LOCKED, 40),
        LessonNode("l11", "u4", 2, "C-suite tone", LessonType.SPEAK_IT, NodeState.LOCKED, 40),
        LessonNode("l12", "u4", 3, "Board room language", LessonType.SPEAK_IT, NodeState.LOCKED, 40),
    ), isLocked = true),
    LearningUnit("u5", "C-Suite Scenarios", 0xFFFFC838, listOf(
        LessonNode("l13", "u5", 1, "Crisis comms", LessonType.SPEAK_IT, NodeState.LOCKED, 50),
        LessonNode("l14", "u5", 2, "Investor pitch", LessonType.SPEAK_IT, NodeState.LOCKED, 50),
        LessonNode("l15", "u5", 3, "Media interview", LessonType.SPEAK_IT, NodeState.LOCKED, 50),
    ), isLocked = true),
)
