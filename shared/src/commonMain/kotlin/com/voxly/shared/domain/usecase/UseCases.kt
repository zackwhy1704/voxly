package com.voxly.shared.domain.usecase

import com.voxly.shared.domain.model.*
import com.voxly.shared.domain.repository.*

class SignUpWithEmailUseCase(private val repo: AuthRepository) {
    suspend operator fun invoke(
        email: String,
        password: String,
        name: String,
        country: String,
        goal: LearningGoal,
    ) = repo.signUpWithEmail(email, password, name, country, goal)
}

class LogInUseCase(private val repo: AuthRepository) {
    suspend operator fun invoke(email: String, password: String) =
        repo.logIn(email, password)
}

class LogOutUseCase(private val repo: AuthRepository) {
    suspend operator fun invoke() = repo.logOut()
}

class SendOtpUseCase(private val repo: AuthRepository) {
    suspend operator fun invoke(email: String) = repo.sendOtp(email)
}

class VerifyOtpUseCase(private val repo: AuthRepository) {
    suspend operator fun invoke(email: String, code: String) = repo.verifyOtp(email, code)
}

class ResetPasswordUseCase(private val repo: AuthRepository) {
    suspend operator fun invoke(email: String) = repo.resetPassword(email)
}

class GetCurrentUserFlowUseCase(private val repo: AuthRepository) {
    operator fun invoke() = repo.currentUser()
}

class GetScenariosUseCase(private val repo: ScenarioRepository) {
    suspend operator fun invoke() = repo.getAllScenarios()
}

class GetRecommendedScenariosUseCase(private val repo: ScenarioRepository) {
    suspend operator fun invoke(userId: String) = repo.getRecommended(userId)
}

class GetScenarioByIdUseCase(private val repo: ScenarioRepository) {
    suspend operator fun invoke(id: String) = repo.getScenarioById(id)
}

class SearchScenariosUseCase(private val repo: ScenarioRepository) {
    suspend operator fun invoke(query: String) = repo.searchScenarios(query)
}

class CreateSessionUseCase(private val repo: SessionRepository) {
    suspend operator fun invoke(scenarioId: String, userId: String) =
        repo.createSession(scenarioId, userId)
}

class CompleteSessionUseCase(private val repo: SessionRepository) {
    suspend operator fun invoke(session: Session) = repo.completeSession(session)
}

class GetSessionHistoryUseCase(private val repo: SessionRepository) {
    suspend operator fun invoke(userId: String) = repo.getSessionHistory(userId)
}

class GetLearningPathUseCase(private val repo: LearningRepository) {
    suspend operator fun invoke(userId: String) = repo.getLearningPath(userId)
}

class GetDailyChallengesUseCase(private val repo: LearningRepository) {
    suspend operator fun invoke() = repo.getDailyChallenges()
}

class CompleteChallengeUseCase(private val repo: LearningRepository) {
    suspend operator fun invoke(challengeId: String) = repo.completeChallenge(challengeId)
}

class AnalyseAudioUseCase(private val repo: SpeechRepository) {
    suspend operator fun invoke(audioData: ByteArray, sessionId: String) =
        repo.analyseAudio(audioData, sessionId)
}
