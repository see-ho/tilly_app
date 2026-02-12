package com.seeho.tilly.core.network.model

/**
 * OpenAI TIL 분석 결과 모델
 * AI가 분석한 감정, 난이도, 피드백, 태그 정보를 담는 데이터 클래스
 */
data class TilAnalysisResult(
    val emotion: String,
    val emotionScore: Int,
    val difficultyLevel: String,
    val feedback: String,
    val tags: List<String>
)
