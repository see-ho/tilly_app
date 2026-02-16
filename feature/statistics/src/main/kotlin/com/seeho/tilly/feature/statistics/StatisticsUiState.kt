package com.seeho.tilly.feature.statistics

import androidx.compose.ui.graphics.Color
import java.time.LocalDate

/**
 * Statistics 화면의 UI 상태
 */
data class StatisticsUiState(
    // 월 선택기 상태
    val currentMonth: Int = LocalDate.now().monthValue,
    val currentYear: Int = LocalDate.now().year,
    val tilCount: Int = 3,
    val canGoNext: Boolean = false,
    
    // 데이터 로딩 상태
    val isLoading: Boolean = false,

    // 감정 추세 데이터 (일별 감정 점수)
    val emotionTrendData: List<EmotionTrendItem> = emptyList(),
    val daysInMonth: Int = java.time.YearMonth.now().lengthOfMonth(),

    // 학습 키워드 데이터 (키워드 빈도수)
    val learningKeywords: List<LearningKeywordItem> = emptyList(),

    // 감정 분포 데이터 (도넛 차트)
    val emotionDistribution: List<EmotionDistributionItem> = emptyList(),

    // 월간 회고
    val retrospectiveText: String = "",
    val isRetrospectiveLoading: Boolean = false,
    val hasRetrospective: Boolean = false,
)

/**
 * 감정 추세 아이템
 */
data class EmotionTrendItem(
    val day: Int,         // 일자 (1~31)
    val score: Float,     // 0~5
)

/**
 * 학습 키워드 아이템 (도넛 차트용)
 */
data class LearningKeywordItem(
    val keyword: String,  // 키워드
    val count: Float,     // 빈도수
    val percent: Int,     // 비율 (%)
    val color: Color,     // 차트 색상
)

/**
 * 감정 분포 아이템 (막대 차트용)
 */
data class EmotionDistributionItem(
    val label: String,    // 성취, 보통, 성장...
    val count: Float,     // 빈도수
    val color: Color,     // 차트 색상
)

