package com.voxly.shared.data.remote

import com.voxly.shared.domain.model.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// ── DTOs ─────────────────────────────────────────────────────────────────────

@Serializable
data class SignUpEmailRequest(
    val email: String,
    val password: String,
    val name: String,
    val country: String,
    val goal: String,
)

@Serializable
data class LoginRequest(val email: String, val password: String)

@Serializable
data class OtpRequest(val email: String)

@Serializable
data class OtpVerifyRequest(val email: String, val code: String)

@Serializable
data class AuthResponse(val token: String, val user: UserDto)

@Serializable
data class UserDto(
    val id: String,
    val name: String,
    val email: String,
    val country: String,
    val goal: String,
    @SerialName("career_readiness_score") val careerReadinessScore: Int = 0,
    @SerialName("current_tier") val currentTier: String = "BRONZE",
    @SerialName("streak_days") val streakDays: Int = 0,
    @SerialName("total_xp") val totalXp: Int = 0,
    @SerialName("weekly_xp") val weeklyXp: Int = 0,
    val rank: String = "Bronze I",
    @SerialName("created_at") val createdAt: Long = 0L,
) {
    fun toDomain() = User(
        id = id, name = name, email = email, country = country,
        goal = runCatching { LearningGoal.valueOf(goal) }.getOrElse { LearningGoal.CAREER_GROWTH },
        currentLevel = runCatching { ProficiencyLevel.valueOf(currentTier) }.getOrElse { ProficiencyLevel.BRONZE },
        careerReadinessScore = careerReadinessScore,
        currentTier = runCatching { DifficultyTier.valueOf(currentTier) }.getOrElse { DifficultyTier.BRONZE },
        streakDays = streakDays, totalXP = totalXp, weeklyXP = weeklyXp,
        rank = rank, createdAt = createdAt,
    )
}

@Serializable
data class ScenarioDto(
    val id: String,
    val title: String,
    val subtitle: String,
    val tier: String,
    val category: String,
    val description: String,
    @SerialName("accent_color_hex") val accentColorHex: Long = 0xFFCC8833,
    @SerialName("xp_reward") val xpReward: Int = 30,
    @SerialName("is_locked") val isLocked: Boolean = false,
    @SerialName("duration_minutes") val durationMinutes: Int = 15,
    @SerialName("progress_fraction") val progressFraction: Float = 0f,
) {
    fun toDomain() = Scenario(
        id = id, title = title, subtitle = subtitle,
        tier = runCatching { DifficultyTier.valueOf(tier) }.getOrElse { DifficultyTier.BRONZE },
        durationMinutes = durationMinutes,
        category = runCatching { ScenarioCategory.valueOf(category) }.getOrElse { ScenarioCategory.INTERVIEW },
        description = description, accentColorHex = accentColorHex,
        xpReward = xpReward, isLocked = isLocked, progressFraction = progressFraction,
    )
}

// ── Services ──────────────────────────────────────────────────────────────────

class AuthApiService(private val client: HttpClient) {
    private val base = "${VoxlyConfig.API_V1}/auth"

    suspend fun signUpWithEmail(req: SignUpEmailRequest): AuthResponse =
        client.post("$base/signup/email") { setBody(req) }.body()

    suspend fun login(req: LoginRequest): AuthResponse =
        client.post("$base/login") { setBody(req) }.body()

    suspend fun sendOtp(req: OtpRequest): Unit =
        client.post("$base/otp/send") { setBody(req) }.body()

    suspend fun verifyOtp(req: OtpVerifyRequest): AuthResponse =
        client.post("$base/otp/verify") { setBody(req) }.body()

    suspend fun resetPassword(req: OtpRequest): Unit =
        client.post("$base/password/reset") { setBody(req) }.body()

    suspend fun me(): UserDto =
        client.get("$base/me").body()
}

class ScenarioApiService(private val client: HttpClient) {
    private val base = "${VoxlyConfig.API_V1}/scenarios"

    suspend fun getAll(): List<ScenarioDto> = client.get(base).body()
    suspend fun getRecommended(userId: String): List<ScenarioDto> =
        client.get("$base/recommended") { parameter("user_id", userId) }.body()
    suspend fun getById(id: String): ScenarioDto = client.get("$base/$id").body()
}

class SessionApiService(private val client: HttpClient) {
    private val base = "${VoxlyConfig.API_V1}/sessions"

    suspend fun create(scenarioId: String, userId: String): Session =
        client.post(base) {
            setBody(mapOf("scenario_id" to scenarioId, "user_id" to userId))
        }.body()

    suspend fun getById(id: String): Session = client.get("$base/$id").body()

    suspend fun complete(id: String, session: Session): Session =
        client.patch("$base/$id/complete") { setBody(session) }.body()

    suspend fun getFeedback(id: String): List<String> =
        client.post("$base/$id/feedback").body()

    suspend fun getForUser(userId: String): List<Session> =
        client.get("${VoxlyConfig.API_V1}/users/$userId/sessions").body()
}

class LearningApiService(private val client: HttpClient) {
    private val base = "${VoxlyConfig.API_V1}/learning"

    suspend fun getPath(userId: String): List<LearningUnit> =
        client.get("$base/path") { parameter("user_id", userId) }.body()

    suspend fun getDailyChallenges(): List<DailyChallenge> =
        client.get("$base/challenges/daily").body()

    suspend fun completeChallenge(id: String): DailyChallenge =
        client.post("$base/challenges/$id/complete").body()
}
