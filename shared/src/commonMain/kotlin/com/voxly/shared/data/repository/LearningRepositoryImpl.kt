package com.voxly.shared.data.repository

import com.voxly.shared.data.remote.LearningApiService
import com.voxly.shared.domain.model.*
import com.voxly.shared.domain.repository.LearningRepository

class LearningRepositoryImpl(
    private val api: LearningApiService,
) : LearningRepository {

    override suspend fun getLearningPath(userId: String): Result<List<LearningUnit>> =
        runCatching { api.getPath(userId) }

    override suspend fun completeLesson(lessonId: String, score: ScoreBreakdown): Result<LessonNode> =
        runCatching { throw NotImplementedError("POST /learning/lessons/$lessonId/complete") }

    override suspend fun getDailyChallenges(): Result<List<DailyChallenge>> =
        runCatching { api.getDailyChallenges() }

    override suspend fun completeChallenge(challengeId: String): Result<DailyChallenge> =
        runCatching { api.completeChallenge(challengeId) }
}
