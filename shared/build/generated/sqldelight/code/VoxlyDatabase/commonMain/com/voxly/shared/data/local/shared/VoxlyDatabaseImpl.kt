package com.voxly.shared.`data`.local.shared

import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.AfterVersion
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import com.voxly.shared.`data`.local.VoxlyDatabase
import com.voxly.shared.`data`.local.VoxlyDatabaseQueries
import kotlin.Long
import kotlin.Unit
import kotlin.reflect.KClass

internal val KClass<VoxlyDatabase>.schema: SqlSchema<QueryResult.Value<Unit>>
  get() = VoxlyDatabaseImpl.Schema

internal fun KClass<VoxlyDatabase>.newInstance(driver: SqlDriver): VoxlyDatabase =
    VoxlyDatabaseImpl(driver)

private class VoxlyDatabaseImpl(
  driver: SqlDriver,
) : TransacterImpl(driver), VoxlyDatabase {
  override val voxlyDatabaseQueries: VoxlyDatabaseQueries = VoxlyDatabaseQueries(driver)

  public object Schema : SqlSchema<QueryResult.Value<Unit>> {
    override val version: Long
      get() = 1

    override fun create(driver: SqlDriver): QueryResult.Value<Unit> {
      driver.execute(null, """
          |CREATE TABLE UserEntity (
          |    id TEXT PRIMARY KEY,
          |    name TEXT NOT NULL,
          |    email TEXT NOT NULL,
          |    country TEXT NOT NULL,
          |    goal TEXT NOT NULL,
          |    currentLevel TEXT NOT NULL,
          |    careerReadinessScore INTEGER NOT NULL DEFAULT 0,
          |    currentTier TEXT NOT NULL DEFAULT 'BRONZE',
          |    streakDays INTEGER NOT NULL DEFAULT 0,
          |    totalXP INTEGER NOT NULL DEFAULT 0,
          |    weeklyXP INTEGER NOT NULL DEFAULT 0,
          |    rank TEXT NOT NULL DEFAULT 'Bronze I',
          |    updatedAt INTEGER NOT NULL
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE SessionEntity (
          |    id TEXT PRIMARY KEY,
          |    scenarioId TEXT NOT NULL,
          |    userId TEXT NOT NULL,
          |    startedAt INTEGER NOT NULL,
          |    completedAt INTEGER,
          |    overallScore INTEGER NOT NULL DEFAULT 0,
          |    pronunciationScore INTEGER NOT NULL DEFAULT 0,
          |    fluencyScore INTEGER NOT NULL DEFAULT 0,
          |    grammarScore INTEGER NOT NULL DEFAULT 0,
          |    vocabularyScore INTEGER NOT NULL DEFAULT 0,
          |    xpEarned INTEGER NOT NULL DEFAULT 0,
          |    aiInsightsJson TEXT NOT NULL DEFAULT '[]',
          |    flaggedWordsJson TEXT NOT NULL DEFAULT '[]'
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE ChallengeEntity (
          |    id TEXT PRIMARY KEY,
          |    title TEXT NOT NULL,
          |    description TEXT NOT NULL,
          |    xpReward INTEGER NOT NULL,
          |    accentColorHex INTEGER NOT NULL,
          |    isCompleted INTEGER NOT NULL DEFAULT 0,
          |    resetsAt INTEGER NOT NULL
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE StreakEntity (
          |    userId TEXT PRIMARY KEY,
          |    currentStreak INTEGER NOT NULL DEFAULT 0,
          |    lastActivityDate TEXT NOT NULL,
          |    longestStreak INTEGER NOT NULL DEFAULT 0
          |)
          """.trimMargin(), 0)
      return QueryResult.Unit
    }

    override fun migrate(
      driver: SqlDriver,
      oldVersion: Long,
      newVersion: Long,
      vararg callbacks: AfterVersion,
    ): QueryResult.Value<Unit> = QueryResult.Unit
  }
}
