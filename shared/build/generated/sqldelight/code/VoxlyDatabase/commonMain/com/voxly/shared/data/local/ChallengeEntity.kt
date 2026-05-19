package com.voxly.shared.`data`.local

import kotlin.Long
import kotlin.String

public data class ChallengeEntity(
  public val id: String,
  public val title: String,
  public val description: String,
  public val xpReward: Long,
  public val accentColorHex: Long,
  public val isCompleted: Long,
  public val resetsAt: Long,
)
