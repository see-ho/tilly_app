package com.seeho.tilly.core.model

/**
 * TIL(Today I Learned)의 핵심 도메인 모델
 */
data class Til(
    val id: Long = 0,                    // 자동 생성 ID (Auto-increment PK)
    val title: String,                   // TIL 제목
    val learned: String,                 // 오늘 배운 것 (필수)
    val difficulty: String? = null,      // 어려웠던 점 (선택)
    val tomorrow: String? = null,        // 내일 할 일 (선택)
    val tags: List<String> = emptyList(), // 태그 리스트
    val emotion: String? = null,         // AI 분석: 감정 키워드
    val emotionScore: Int? = null,       // AI 분석: 감정 점수 (1-5)
    val difficultyLevel: String? = null,  // AI 분석: 체감 난이도
    val createdAt: Long,                 // 생성 일시 (milliseconds timestamp)
    val updatedAt: Long? = null          // 수정 일시 (milliseconds timestamp)
)
