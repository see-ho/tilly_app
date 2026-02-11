package com.seeho.tilly.core.model

data class AiAnalysisResult(
    val tags: List<String>,
    val emotion: String,
    val emotionScore: Int,
    val difficultyLevel: String,
    val feedback: String
)
