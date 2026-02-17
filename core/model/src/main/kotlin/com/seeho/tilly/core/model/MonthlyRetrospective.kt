package com.seeho.tilly.core.model

data class MonthlyRetrospective(
    val id: Long = 0,
    val month: Int,
    val year: Int,
    val summary: String,                        // 한 줄 요약
    val growthPoints: List<String>,             // 성장 포인트
    val improvementPoints: List<String>,        // 개선 포인트
    val nextMonthSuggestions: List<String>,     // 다음 달 추천
    val createdAt: Long,                        // 생성 일시
)
