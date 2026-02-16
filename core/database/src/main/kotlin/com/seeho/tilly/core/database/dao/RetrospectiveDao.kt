package com.seeho.tilly.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.seeho.tilly.core.database.entity.RetrospectiveEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RetrospectiveDao {

    @Query("SELECT * FROM retrospectives WHERE month = :month AND year = :year LIMIT 1")
    fun getRetrospective(month: Int, year: Int): Flow<RetrospectiveEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRetrospective(entity: RetrospectiveEntity)
}
