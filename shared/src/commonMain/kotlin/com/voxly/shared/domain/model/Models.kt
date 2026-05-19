package com.voxly.shared.domain.model

import kotlinx.serialization.Serializable

// ── Enums ─────────────────────────────────────────────────────────────────────

enum class LearningGoal(val displayName: String) {
    CAREER_GROWTH("Career growth & interviews"),
    MNC_MEETINGS("MNC meetings & client work"),
    PRESENTATIONS("Presentations & public speaking"),
    GENERAL_CONFIDENCE("General professional confidence"),
}

enum class ProficiencyLevel { BRONZE, SILVER, GOLD, PLATINUM, DIAMOND }

enum class DifficultyTier(val displayName: String, val colorHex: Long) {
    BRONZE("Bronze",   0xFFCC8833),
    SILVER("Silver",   0xFFAAAAAA),
    GOLD("Gold",       0xFFFFC838),
    PLATINUM("Plat",   0xFF80CCDD),
    DIAMOND("Diamond", 0xFF6699FF),
}

enum class ScenarioCategory(val displayName: String) {
    INTERVIEW("Interviews"),
    MEETING("Meetings"),
    PITCH("Pitches"),
    NEGOTIATION("Negotiations"),
    PRESENTATION("Presentations"),
    NETWORKING("Networking"),
}

enum class ModifierType(val label: String, val icon: String) {
    HOSTILE_MODE("Hostile mode", "⚡"),
    TIME_PRESSURE("Time pressure", "⏱"),
    DATA_REQUIRED("Data required", "📊"),
}

enum class LessonType { SPEAK_IT, TAP_WHAT_YOU_HEAR, FILL_IN, PRONUNCIATION_DRILL }

enum class NodeState { LOCKED, AVAILABLE, IN_PROGRESS, COMPLETED }

// ── Data Models ───────────────────────────────────────────────────────────────

@Serializable
data class User(
    val id: String,
    val name: String,
    val email: String,
    val country: String,
    val goal: LearningGoal,
    val currentLevel: ProficiencyLevel,
    val careerReadinessScore: Int,
    val currentTier: DifficultyTier,
    val streakDays: Int,
    val totalXP: Int,
    val weeklyXP: Int,
    val rank: String,
    val createdAt: Long,
)

@Serializable
data class ChallengeModifier(
    val type: ModifierType,
    val isEnabled: Boolean,
)

@Serializable
data class Scenario(
    val id: String,
    val title: String,
    val subtitle: String,
    val tier: DifficultyTier,
    val durationMinutes: Int,
    val category: ScenarioCategory,
    val description: String,
    val accentColorHex: Long,
    val xpReward: Int,
    val modifiers: List<ChallengeModifier> = emptyList(),
    val skillsTested: List<String> = emptyList(),
    val progressFraction: Float = 0f,
    val isLocked: Boolean = false,
)

@Serializable
data class FillerWordEvent(
    val word: String,
    val timestampSeconds: Int,
)

@Serializable
data class FlaggedWord(
    val word: String,
    val userPronunciation: String,
    val targetPronunciation: String,
    val stressNote: String,
)

@Serializable
data class ScoreBreakdown(
    val pronunciation: Int,
    val fluency: Int,
    val grammar: Int,
    val vocabulary: Int,
) {
    val overall: Int get() = (pronunciation + fluency + grammar + vocabulary) / 4
}

@Serializable
data class Session(
    val id: String,
    val scenarioId: String,
    val userId: String,
    val startedAt: Long,
    val completedAt: Long? = null,
    val durationSeconds: Int = 0,
    val overallScore: Int = 0,
    val pronunciationScore: Int = 0,
    val fluencyScore: Int = 0,
    val grammarScore: Int = 0,
    val vocabularyScore: Int = 0,
    val xpEarned: Int = 0,
    val fillerWords: List<FillerWordEvent> = emptyList(),
    val pacingData: List<Float> = emptyList(),
    val flaggedWords: List<FlaggedWord> = emptyList(),
    val aiInsights: List<String> = emptyList(),
) {
    val scoreBreakdown: ScoreBreakdown
        get() = ScoreBreakdown(pronunciationScore, fluencyScore, grammarScore, vocabularyScore)
}

@Serializable
data class LessonNode(
    val id: String,
    val unitId: String,
    val position: Int,
    val title: String,
    val type: LessonType,
    val state: NodeState,
    val xpReward: Int,
)

@Serializable
data class LearningUnit(
    val id: String,
    val title: String,
    val colorHex: Long,
    val nodes: List<LessonNode>,
    val isLocked: Boolean,
)

@Serializable
data class DailyChallenge(
    val id: String,
    val title: String,
    val description: String,
    val xpReward: Int,
    val accentColorHex: Long,
    val isCompleted: Boolean,
    val resetsAt: Long,
)

data class HUDState(
    val pace: String = "good",
    val fillerCount: Int = 0,
    val clarity: String = "high",
    val elapsedSeconds: Int = 0,
)

data class LiveSessionState(
    val questionIndex: Int = 0,
    val totalQuestions: Int = 8,
    val currentQuestion: String = "",
    val isListening: Boolean = false,
    val amplitudes: List<Float> = emptyList(),
    val hud: HUDState = HUDState(),
    val isPaused: Boolean = false,
)
