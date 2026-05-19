package com.voxly.shared.data.repository

import com.voxly.shared.data.remote.ScenarioApiService
import com.voxly.shared.domain.model.*
import com.voxly.shared.domain.repository.ScenarioRepository

class ScenarioRepositoryImpl(
    private val api: ScenarioApiService,
) : ScenarioRepository {

    private var cache: List<Scenario> = emptyList()

    override suspend fun getAllScenarios(): Result<List<Scenario>> = runCatching {
        val scenarios = api.getAll().map { it.toDomain() }
        cache = scenarios
        scenarios
    }

    override suspend fun getScenariosByTier(tier: DifficultyTier): Result<List<Scenario>> =
        runCatching { ensureCache().filter { it.tier == tier } }

    override suspend fun getScenariosByCategory(category: ScenarioCategory): Result<List<Scenario>> =
        runCatching { ensureCache().filter { it.category == category } }

    override suspend fun getScenarioById(id: String): Result<Scenario> = runCatching {
        cache.firstOrNull { it.id == id } ?: api.getById(id).toDomain()
    }

    override suspend fun getRecommended(userId: String): Result<List<Scenario>> = runCatching {
        api.getRecommended(userId).map { it.toDomain() }
    }

    override suspend fun searchScenarios(query: String): Result<List<Scenario>> =
        runCatching { ensureCache().filter { it.title.contains(query, ignoreCase = true) } }

    private suspend fun ensureCache(): List<Scenario> {
        if (cache.isEmpty()) cache = api.getAll().map { it.toDomain() }
        return cache
    }
}
