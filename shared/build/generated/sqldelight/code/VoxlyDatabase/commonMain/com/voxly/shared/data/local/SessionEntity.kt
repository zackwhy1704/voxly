package com.voxly.shared.`data`.local

import kotlin.Long
import kotlin.String

public data class SessionEntity(
  public val id: String,
  public val scenarioId: String,
  public val userId: String,
  public val startedAt: Long,
  public val completedAt: Long?,
  public val overallScore: Long,
  public val pronunciationScore: Long,
  public val fluencyScore: Long,
  public val grammarScore: Long,
  public val vocabularyScore: Long,
  public val xpEarned: Long,
  public val aiInsightsJson: String,
  public val flaggedWordsJson: String,
)
