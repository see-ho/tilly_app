package com.seeho.tilly.core.data.repository

import com.seeho.tilly.core.domain.repository.AiAnalysisRepository
import com.seeho.tilly.core.model.AiAnalysisResult
import com.seeho.tilly.core.network.OpenAIService
import javax.inject.Inject

class AiAnalysisRepositoryImpl @Inject constructor(
    private val openAIService: OpenAIService
) : AiAnalysisRepository {
    override suspend fun analyzeTil(
        title: String,
        learned: String,
        difficulty: String?,
        tomorrow: String?
    ): Result<AiAnalysisResult> {
        // OpenAIService 호출 후 Network Model → Domain Model 변환
        return openAIService.analyzeTil(title, learned, difficulty, tomorrow)
            .map { result ->
                AiAnalysisResult(
                    tags = result.tags,
                    emotion = result.emotion,
                    emotionScore = result.emotionScore,
                    difficultyLevel = result.difficultyLevel,
                    feedback = result.feedback
                )
            }
    }
}
