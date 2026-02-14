package com.seeho.tilly.core.model

data class AiAnalysisResult(
    val tags: List<String>,
    val emotion: Emotion,
    val emotionScore: Int,
    val difficultyLevel: Difficulty,
    val feedback: String
)
