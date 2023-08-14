package com.frhanklin.disastory.data.local.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import com.frhanklin.disastory.data.local.entity.DisasterModel

@Dao
interface DisasterDao {

    @RawQuery(observedEntities = [DisasterModel::class])
    fun getAllDisaster(query: SimpleSQLiteQuery): DataSource.Factory<Int, DisasterModel>

    @Query("SELECT * FROM disaster WHERE created_at >= :startDate AND created_at <= :endDate")
    fun getDisasterRange(startDate: String, endDate: String): DataSource.Factory<Int, DisasterModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertDisasters(disasters: List<DisasterModel>)


}