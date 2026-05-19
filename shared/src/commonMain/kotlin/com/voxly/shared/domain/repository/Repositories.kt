package com.voxly.shared.domain.repository

import com.voxly.shared.domain.model.*
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signUpWithEmail(
        email: String,
        password: String,
        name: String,
        country: String,
        goal: LearningGoal,
    ): Result<User>

    suspend fun signUpWithGoogle(idToken: String): Result<User>
    suspend fun logIn(email: String, password: String): Result<User>
    suspend fun logOut(): Result<Unit>
    suspend fun sendOtp(email: String): Result<Unit>
    suspend fun verifyOtp(email: String, code: String): Result<User>
    suspend fun resetPassword(email: String): Result<Unit>
    fun currentUser(): Flow<User?>
}

interface UserRepository {
    suspend fun getUser(id: String): Result<User>
    suspend fun updateGoal(goal: LearningGoal): Result<User>
    suspend fun updateProfile(name: String, country: String): Result<User>
    fun observeUser(): Flow<User>
}

interface ScenarioRepository {
    suspend fun getAllScenarios(): Result<List<Scenario>>
    suspend fun getScenariosByTier(tier: DifficultyTier): Result<List<Scenario>>
    suspend fun getScenariosByCategory(category: ScenarioCategory): Result<List<Scenario>>
    suspend fun getScenarioById(id: String): Result<Scenario>
    suspend fun getRecommended(userId: String): Result<List<Scenario>>
    suspend fun searchScenarios(query: String): Result<List<Scenario>>
}

interface SessionRepository {
    suspend fun createSession(scenarioId: String, userId: String): Result<Session>
    suspend fun completeSession(session: Session): Result<Session>
    suspend fun getSessionHistory(userId: String): Result<List<Session>>
    suspend fun getSessionById(id: String): Result<Session>
}

interface LearningRepository {
    suspend fun getLearningPath(userId: String): Result<List<LearningUnit>>
    suspend fun completeLesson(lessonId: String, score: ScoreBreakdown): Result<LessonNode>
    suspend fun getDailyChallenges(): Result<List<DailyChallenge>>
    suspend fun completeChallenge(challengeId: String): Result<DailyChallenge>
}

interface SpeechRepository {
    suspend fun analyseAudio(audioData: ByteArray, sessionId: String): Result<ScoreBreakdown>
    fun streamHUD(sessionId: String): Flow<HUDState>
}
