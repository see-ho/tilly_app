package com.seeho.tilly.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.seeho.tilly.core.database.entity.TilEntity
import kotlinx.coroutines.flow.Flow

/**
 * TIL 데이터 접근 객체 (DAO)
 */
@Dao
interface TilDao {

    /**
     * 전체 TIL 목록 조회 (최신순 정렬)
     */
    @Query("SELECT * FROM tils ORDER BY createdAt DESC")
    fun getAllTils(): Flow<List<TilEntity>>

    /**
     * ID로 단일 TIL 조회
     */
    @Query("SELECT * FROM tils WHERE id = :id")
    fun getTilById(id: Long): Flow<TilEntity?>

    /**
     * TIL 저장 (새로 생성)
     * @return 생성된 row의 ID
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTil(til: TilEntity): Long

    /**
     * TIL 수정
     */
    @Update
    suspend fun updateTil(til: TilEntity)

    /**
     * TIL 삭제 (ID 기반)
     */
    @Query("DELETE FROM tils WHERE id = :id")
    suspend fun deleteTilById(id: Long)
}
