package com.voxly.shared.`data`.local

import app.cash.sqldelight.Query
import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import kotlin.Any
import kotlin.Long
import kotlin.String

public class VoxlyDatabaseQueries(
  driver: SqlDriver,
) : TransacterImpl(driver) {
  public fun <T : Any> getUser(id: String, mapper: (
    id: String,
    name: String,
    email: String,
    country: String,
    goal: String,
    currentLevel: String,
    careerReadinessScore: Long,
    currentTier: String,
    streakDays: Long,
    totalXP: Long,
    weeklyXP: Long,
    rank: String,
    updatedAt: Long,
  ) -> T): Query<T> = GetUserQuery(id) { cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4)!!,
      cursor.getString(5)!!,
      cursor.getLong(6)!!,
      cursor.getString(7)!!,
      cursor.getLong(8)!!,
      cursor.getLong(9)!!,
      cursor.getLong(10)!!,
      cursor.getString(11)!!,
      cursor.getLong(12)!!
    )
  }

  public fun getUser(id: String): Query<UserEntity> = getUser(id) { id_, name, email, country, goal,
      currentLevel, careerReadinessScore, currentTier, streakDays, totalXP, weeklyXP, rank,
      updatedAt ->
    UserEntity(
      id_,
      name,
      email,
      country,
      goal,
      currentLevel,
      careerReadinessScore,
      currentTier,
      streakDays,
      totalXP,
      weeklyXP,
      rank,
      updatedAt
    )
  }

  public fun <T : Any> getSessionsForUser(userId: String, mapper: (
    id: String,
    scenarioId: String,
    userId: String,
    startedAt: Long,
    completedAt: Long?,
    overallScore: Long,
    pronunciationScore: Long,
    fluencyScore: Long,
    grammarScore: Long,
    vocabularyScore: Long,
    xpEarned: Long,
    aiInsightsJson: String,
    flaggedWordsJson: String,
  ) -> T): Query<T> = GetSessionsForUserQuery(userId) { cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getLong(3)!!,
      cursor.getLong(4),
      cursor.getLong(5)!!,
      cursor.getLong(6)!!,
      cursor.getLong(7)!!,
      cursor.getLong(8)!!,
      cursor.getLong(9)!!,
      cursor.getLong(10)!!,
      cursor.getString(11)!!,
      cursor.getString(12)!!
    )
  }

  public fun getSessionsForUser(userId: String): Query<SessionEntity> = getSessionsForUser(userId) {
      id, scenarioId, userId_, startedAt, completedAt, overallScore, pronunciationScore,
      fluencyScore, grammarScore, vocabularyScore, xpEarned, aiInsightsJson, flaggedWordsJson ->
    SessionEntity(
      id,
      scenarioId,
      userId_,
      startedAt,
      completedAt,
      overallScore,
      pronunciationScore,
      fluencyScore,
      grammarScore,
      vocabularyScore,
      xpEarned,
      aiInsightsJson,
      flaggedWordsJson
    )
  }

  public fun <T : Any> getSessionById(id: String, mapper: (
    id: String,
    scenarioId: String,
    userId: String,
    startedAt: Long,
    completedAt: Long?,
    overallScore: Long,
    pronunciationScore: Long,
    fluencyScore: Long,
    grammarScore: Long,
    vocabularyScore: Long,
    xpEarned: Long,
    aiInsightsJson: String,
    flaggedWordsJson: String,
  ) -> T): Query<T> = GetSessionByIdQuery(id) { cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getLong(3)!!,
      cursor.getLong(4),
      cursor.getLong(5)!!,
      cursor.getLong(6)!!,
      cursor.getLong(7)!!,
      cursor.getLong(8)!!,
      cursor.getLong(9)!!,
      cursor.getLong(10)!!,
      cursor.getString(11)!!,
      cursor.getString(12)!!
    )
  }

  public fun getSessionById(id: String): Query<SessionEntity> = getSessionById(id) { id_,
      scenarioId, userId, startedAt, completedAt, overallScore, pronunciationScore, fluencyScore,
      grammarScore, vocabularyScore, xpEarned, aiInsightsJson, flaggedWordsJson ->
    SessionEntity(
      id_,
      scenarioId,
      userId,
      startedAt,
      completedAt,
      overallScore,
      pronunciationScore,
      fluencyScore,
      grammarScore,
      vocabularyScore,
      xpEarned,
      aiInsightsJson,
      flaggedWordsJson
    )
  }

  public fun <T : Any> getAllChallenges(mapper: (
    id: String,
    title: String,
    description: String,
    xpReward: Long,
    accentColorHex: Long,
    isCompleted: Long,
    resetsAt: Long,
  ) -> T): Query<T> = Query(-1_887_477_129, arrayOf("ChallengeEntity"), driver, "VoxlyDatabase.sq",
      "getAllChallenges",
      "SELECT ChallengeEntity.id, ChallengeEntity.title, ChallengeEntity.description, ChallengeEntity.xpReward, ChallengeEntity.accentColorHex, ChallengeEntity.isCompleted, ChallengeEntity.resetsAt FROM ChallengeEntity ORDER BY resetsAt ASC") {
      cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getLong(3)!!,
      cursor.getLong(4)!!,
      cursor.getLong(5)!!,
      cursor.getLong(6)!!
    )
  }

  public fun getAllChallenges(): Query<ChallengeEntity> = getAllChallenges { id, title, description,
      xpReward, accentColorHex, isCompleted, resetsAt ->
    ChallengeEntity(
      id,
      title,
      description,
      xpReward,
      accentColorHex,
      isCompleted,
      resetsAt
    )
  }

  public fun <T : Any> getStreak(userId: String, mapper: (
    userId: String,
    currentStreak: Long,
    lastActivityDate: String,
    longestStreak: Long,
  ) -> T): Query<T> = GetStreakQuery(userId) { cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getLong(1)!!,
      cursor.getString(2)!!,
      cursor.getLong(3)!!
    )
  }

  public fun getStreak(userId: String): Query<StreakEntity> = getStreak(userId) { userId_,
      currentStreak, lastActivityDate, longestStreak ->
    StreakEntity(
      userId_,
      currentStreak,
      lastActivityDate,
      longestStreak
    )
  }

  public fun upsertUser(UserEntity: UserEntity) {
    driver.execute(-1_330_709_546,
        """INSERT OR REPLACE INTO UserEntity (id, name, email, country, goal, currentLevel, careerReadinessScore, currentTier, streakDays, totalXP, weeklyXP, rank, updatedAt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)""",
        13) {
          bindString(0, UserEntity.id)
          bindString(1, UserEntity.name)
          bindString(2, UserEntity.email)
          bindString(3, UserEntity.country)
          bindString(4, UserEntity.goal)
          bindString(5, UserEntity.currentLevel)
          bindLong(6, UserEntity.careerReadinessScore)
          bindString(7, UserEntity.currentTier)
          bindLong(8, UserEntity.streakDays)
          bindLong(9, UserEntity.totalXP)
          bindLong(10, UserEntity.weeklyXP)
          bindString(11, UserEntity.rank)
          bindLong(12, UserEntity.updatedAt)
        }
    notifyQueries(-1_330_709_546) { emit ->
      emit("UserEntity")
    }
  }

  public fun upsertSession(SessionEntity: SessionEntity) {
    driver.execute(1_512_272_555,
        """INSERT OR REPLACE INTO SessionEntity (id, scenarioId, userId, startedAt, completedAt, overallScore, pronunciationScore, fluencyScore, grammarScore, vocabularyScore, xpEarned, aiInsightsJson, flaggedWordsJson) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)""",
        13) {
          bindString(0, SessionEntity.id)
          bindString(1, SessionEntity.scenarioId)
          bindString(2, SessionEntity.userId)
          bindLong(3, SessionEntity.startedAt)
          bindLong(4, SessionEntity.completedAt)
          bindLong(5, SessionEntity.overallScore)
          bindLong(6, SessionEntity.pronunciationScore)
          bindLong(7, SessionEntity.fluencyScore)
          bindLong(8, SessionEntity.grammarScore)
          bindLong(9, SessionEntity.vocabularyScore)
          bindLong(10, SessionEntity.xpEarned)
          bindString(11, SessionEntity.aiInsightsJson)
          bindString(12, SessionEntity.flaggedWordsJson)
        }
    notifyQueries(1_512_272_555) { emit ->
      emit("SessionEntity")
    }
  }

  public fun upsertChallenge(ChallengeEntity: ChallengeEntity) {
    driver.execute(-1_905_149_128,
        """INSERT OR REPLACE INTO ChallengeEntity (id, title, description, xpReward, accentColorHex, isCompleted, resetsAt) VALUES (?, ?, ?, ?, ?, ?, ?)""",
        7) {
          bindString(0, ChallengeEntity.id)
          bindString(1, ChallengeEntity.title)
          bindString(2, ChallengeEntity.description)
          bindLong(3, ChallengeEntity.xpReward)
          bindLong(4, ChallengeEntity.accentColorHex)
          bindLong(5, ChallengeEntity.isCompleted)
          bindLong(6, ChallengeEntity.resetsAt)
        }
    notifyQueries(-1_905_149_128) { emit ->
      emit("ChallengeEntity")
    }
  }

  public fun markChallengeComplete(id: String) {
    driver.execute(-1_805_198_413, """UPDATE ChallengeEntity SET isCompleted = 1 WHERE id = ?""", 1)
        {
          bindString(0, id)
        }
    notifyQueries(-1_805_198_413) { emit ->
      emit("ChallengeEntity")
    }
  }

  public fun upsertStreak(StreakEntity: StreakEntity) {
    driver.execute(1_032_423_625,
        """INSERT OR REPLACE INTO StreakEntity (userId, currentStreak, lastActivityDate, longestStreak) VALUES (?, ?, ?, ?)""",
        4) {
          bindString(0, StreakEntity.userId)
          bindLong(1, StreakEntity.currentStreak)
          bindString(2, StreakEntity.lastActivityDate)
          bindLong(3, StreakEntity.longestStreak)
        }
    notifyQueries(1_032_423_625) { emit ->
      emit("StreakEntity")
    }
  }

  private inner class GetUserQuery<out T : Any>(
    public val id: String,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("UserEntity", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("UserEntity", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-1_054_594_171,
        """SELECT UserEntity.id, UserEntity.name, UserEntity.email, UserEntity.country, UserEntity.goal, UserEntity.currentLevel, UserEntity.careerReadinessScore, UserEntity.currentTier, UserEntity.streakDays, UserEntity.totalXP, UserEntity.weeklyXP, UserEntity.rank, UserEntity.updatedAt FROM UserEntity WHERE id = ?""",
        mapper, 1) {
      bindString(0, id)
    }

    override fun toString(): String = "VoxlyDatabase.sq:getUser"
  }

  private inner class GetSessionsForUserQuery<out T : Any>(
    public val userId: String,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("SessionEntity", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("SessionEntity", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(1_400_059_101,
        """SELECT SessionEntity.id, SessionEntity.scenarioId, SessionEntity.userId, SessionEntity.startedAt, SessionEntity.completedAt, SessionEntity.overallScore, SessionEntity.pronunciationScore, SessionEntity.fluencyScore, SessionEntity.grammarScore, SessionEntity.vocabularyScore, SessionEntity.xpEarned, SessionEntity.aiInsightsJson, SessionEntity.flaggedWordsJson FROM SessionEntity WHERE userId = ? ORDER BY startedAt DESC""",
        mapper, 1) {
      bindString(0, userId)
    }

    override fun toString(): String = "VoxlyDatabase.sq:getSessionsForUser"
  }

  private inner class GetSessionByIdQuery<out T : Any>(
    public val id: String,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("SessionEntity", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("SessionEntity", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-1_397_124_466,
        """SELECT SessionEntity.id, SessionEntity.scenarioId, SessionEntity.userId, SessionEntity.startedAt, SessionEntity.completedAt, SessionEntity.overallScore, SessionEntity.pronunciationScore, SessionEntity.fluencyScore, SessionEntity.grammarScore, SessionEntity.vocabularyScore, SessionEntity.xpEarned, SessionEntity.aiInsightsJson, SessionEntity.flaggedWordsJson FROM SessionEntity WHERE id = ?""",
        mapper, 1) {
      bindString(0, id)
    }

    override fun toString(): String = "VoxlyDatabase.sq:getSessionById"
  }

  private inner class GetStreakQuery<out T : Any>(
    public val userId: String,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("StreakEntity", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("StreakEntity", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(91_326_648,
        """SELECT StreakEntity.userId, StreakEntity.currentStreak, StreakEntity.lastActivityDate, StreakEntity.longestStreak FROM StreakEntity WHERE userId = ?""",
        mapper, 1) {
      bindString(0, userId)
    }

    override fun toString(): String = "VoxlyDatabase.sq:getStreak"
  }
}
