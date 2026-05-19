package com.voxly.shared.data.repository

import com.voxly.shared.data.remote.SessionApiService
import com.voxly.shared.domain.model.Session
import com.voxly.shared.domain.repository.SessionRepository

class SessionRepositoryImpl(
    private val api: SessionApiService,
) : SessionRepository {

    override suspend fun createSession(scenarioId: String, userId: String): Result<Session> =
        runCatching { api.create(scenarioId, userId) }

    override suspend fun completeSession(session: Session): Result<Session> = runCatching {
        api.complete(session.id, session)
    }

    override suspend fun getSessionHistory(userId: String): Result<List<Session>> =
        runCatching { api.getForUser(userId) }

    override suspend fun getSessionById(id: String): Result<Session> =
        runCatching { api.getById(id) }
}
