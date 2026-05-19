package com.voxly.shared.`data`.local

import kotlin.Long
import kotlin.String

public data class UserEntity(
  public val id: String,
  public val name: String,
  public val email: String,
  public val country: String,
  public val goal: String,
  public val currentLevel: String,
  public val careerReadinessScore: Long,
  public val currentTier: String,
  public val streakDays: Long,
  public val totalXP: Long,
  public val weeklyXP: Long,
  public val rank: String,
  public val updatedAt: Long,
)
