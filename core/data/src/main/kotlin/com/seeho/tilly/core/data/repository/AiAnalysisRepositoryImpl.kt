package com.seeho.tilly.core.data.repository

import com.seeho.tilly.core.data.mapper.toEntity
import com.seeho.tilly.core.data.mapper.toModel
import com.seeho.tilly.core.database.dao.RetrospectiveDao
import com.seeho.tilly.core.domain.repository.AiAnalysisRepository
import com.seeho.tilly.core.model.AiAnalysisResult
import com.seeho.tilly.core.model.Difficulty
import com.seeho.tilly.core.model.Emotion
import com.seeho.tilly.core.model.MonthlyRetrospective
import com.seeho.tilly.core.model.Til
import com.seeho.tilly.core.network.OpenAIService
import com.seeho.tilly.core.network.model.TilSummaryForPrompt
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AiAnalysisRepositoryImpl @Inject constructor(
    private val openAIService: OpenAIService,
    private val retrospectiveDao: RetrospectiveDao,
) : AiAnalysisRepository {

    override suspend fun analyzeTil(
        title: String,
        learned: String,
        difficulty: String?,
        tomorrow: String?
    ): Result<AiAnalysisResult> {
        return openAIService.analyzeTil(title, learned, difficulty, tomorrow)
            .map { result ->
                AiAnalysisResult(
                    tags = result.tags,
                    emotion = Emotion.fromLabel(result.emotion),
                    emotionScore = result.emotionScore,
                    difficultyLevel = Difficulty.fromLabel(result.difficultyLevel),
                    feedback = result.feedback
                )
            }
    }

    override suspend fun generateRetrospective(
        month: Int,
        year: Int,
        tils: List<Til>,
    ): Result<MonthlyRetrospective> {
        // Til -> TilSummaryForPrompt 변환
        val summaries = tils.map { til ->
            TilSummaryForPrompt(
                title = til.title,
                learned = til.learned,
                difficulty = til.difficulty,
                emotion = til.emotion?.label ?: "평범",
                emotionScore = til.emotionScore ?: 3,
                difficultyLevel = til.difficultyLevel?.label ?: "NORMAL",
                tags = til.tags,
            )
        }

        return openAIService.generateRetrospective(month, year, summaries)
            .map { result ->
                val retrospective = MonthlyRetrospective(
                    month = month,
                    year = year,
                    summary = result.summary,
                    growthPoints = result.growthPoints,
                    improvementPoints = result.improvementPoints,
                    nextMonthSuggestions = result.nextMonthSuggestions,
                    createdAt = System.currentTimeMillis(),
                )

                // Room에 저장
                retrospectiveDao.insertRetrospective(retrospective.toEntity())

                retrospective
            }
    }

    override fun getRetrospective(month: Int, year: Int): Flow<MonthlyRetrospective?> {
        return retrospectiveDao.getRetrospective(month, year).map { entity ->
            entity?.toModel()
        }
    }
}
