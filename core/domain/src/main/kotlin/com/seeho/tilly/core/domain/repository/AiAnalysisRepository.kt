package com.seeho.tilly.core.domain.repository

import com.seeho.tilly.core.model.AiAnalysisResult

interface AiAnalysisRepository {
    suspend fun analyzeTil(
        title: String,
        learned: String,
        difficulty: String?,
        tomorrow: String?
    ): Result<AiAnalysisResult>
}
