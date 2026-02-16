package com.seeho.tilly.core.network.model

/**
 * OpenAIService에 전달할 TIL 요약 데이터
 * 월간 회고 생성 프롬프트에 사용
 */
data class TilSummaryForPrompt(
    val title: String,
    val learned: String,
    val difficulty: String?,
    val emotion: String,
    val emotionScore: Int,
    val difficultyLevel: String,
    val tags: List<String>,
)
