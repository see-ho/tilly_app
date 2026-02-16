package com.seeho.tilly.core.domain.repository

import com.seeho.tilly.core.model.AiAnalysisResult
import com.seeho.tilly.core.model.MonthlyRetrospective
import com.seeho.tilly.core.model.Til
import kotlinx.coroutines.flow.Flow

interface AiAnalysisRepository {
    // Til 분석
    suspend fun analyzeTil(
        title: String,
        learned: String,
        difficulty: String?,
        tomorrow: String?
    ): Result<AiAnalysisResult>

    // 월간 회고 생성 (GPT 호출 + Room 저장)
    suspend fun generateRetrospective(
        month: Int,
        year: Int,
        tils: List<Til>,
    ): Result<MonthlyRetrospective>

    // Room에서 월간 회고 조회
    fun getRetrospective(month: Int, year: Int): Flow<MonthlyRetrospective?>
}
