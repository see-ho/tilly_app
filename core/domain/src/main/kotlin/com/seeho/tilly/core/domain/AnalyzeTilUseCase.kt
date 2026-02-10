package com.seeho.tilly.core.domain

import javax.inject.Inject
import kotlinx.coroutines.delay

data class AiAnalysisResult(
    val tags: List<String>,
    val emotion: String,
    val emotionScore: Int,
    val difficultyLevel: String,
    val feedback: String
)

/**
 * TIL 내용을 분석하여 AI 결과를 반환하는 UseCase
 */
class AnalyzeTilUseCase @Inject constructor() {
    
    suspend operator fun invoke(
        title: String,
        learned: String,
        difficulty: String?,
        tomorrow: String?
    ): AiAnalysisResult {
        // TODO: 실제 OpenAI  연동
        // 현재는 흐름 테스트를 위해 Mock 데이터 반환
        delay(2000) // 분석 중인 것처럼 보이기 위해 2초 대기
        
        return AiAnalysisResult(
            tags = listOf("Android", "Kotlin", "Jetpack Compose"),
            emotion = "Happy",
            emotionScore = 4,
            difficultyLevel = "NORMAL",
            feedback = "오늘 공부하신 내용이 정말 알차네요! 특히 Compose의 상태 관리 부분을 잘 이해하신 것 같습니다. 내일 계획하신 기능 구현도 문제없이 해내실 거예요! 파이팅입니다! 🌱"
        )
    }
}
