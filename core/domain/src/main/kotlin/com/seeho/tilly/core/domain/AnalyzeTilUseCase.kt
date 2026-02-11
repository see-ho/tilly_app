package com.seeho.tilly.core.domain

import com.seeho.tilly.core.domain.repository.AiAnalysisRepository
import com.seeho.tilly.core.model.AiAnalysisResult
import javax.inject.Inject

/**
 * TIL 내용을 분석하여 AI 결과를 반환하는 UseCase
 */
class AnalyzeTilUseCase @Inject constructor(
    private val aiAnalysisRepository: AiAnalysisRepository
) {
    
    suspend operator fun invoke(
        title: String,
        learned: String,
        difficulty: String?,
        tomorrow: String?
    ): AiAnalysisResult {
        return aiAnalysisRepository.analyzeTil(title, learned, difficulty, tomorrow)
    }
}
