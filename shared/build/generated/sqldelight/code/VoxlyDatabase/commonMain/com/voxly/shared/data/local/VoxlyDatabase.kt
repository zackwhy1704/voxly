package com.voxly.shared.`data`.local

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import com.voxly.shared.`data`.local.shared.newInstance
import com.voxly.shared.`data`.local.shared.schema
import kotlin.Unit

public interface VoxlyDatabase : Transacter {
  public val voxlyDatabaseQueries: VoxlyDatabaseQueries

  public companion object {
    public val Schema: SqlSchema<QueryResult.Value<Unit>>
      get() = VoxlyDatabase::class.schema

    public operator fun invoke(driver: SqlDriver): VoxlyDatabase =
        VoxlyDatabase::class.newInstance(driver)
  }
}
