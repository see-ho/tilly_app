package com.seeho.tilly.core.domain.repository

import com.seeho.tilly.core.model.Til
import kotlinx.coroutines.flow.Flow

/**
 * TIL Repository 인터페이스
 * Clean Architecture 원칙에 따라 domain 레이어에 정의,
 * 구현체는 data 레이어에 위치
 */
interface TilRepository {

    /** 전체 TIL 목록 조회 (최신순) */
    fun getAllTils(): Flow<List<Til>>

    /** ID로 단일 TIL 조회 */
    fun getTilById(id: Long): Flow<Til?>

    /** TIL 새로 저장 (Create) */
    suspend fun insertTil(til: Til): Long

    /** TIL 수정 (Update) */
    suspend fun updateTil(til: Til)

    /** TIL 삭제 (Delete) */
    suspend fun deleteTilById(id: Long)
}
