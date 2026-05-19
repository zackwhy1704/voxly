package com.voxly.shared.`data`.local

import kotlin.Long
import kotlin.String

public data class StreakEntity(
  public val userId: String,
  public val currentStreak: Long,
  public val lastActivityDate: String,
  public val longestStreak: Long,
)
