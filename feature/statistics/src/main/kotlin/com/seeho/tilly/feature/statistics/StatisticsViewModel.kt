package com.seeho.tilly.feature.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seeho.tilly.core.common.util.DateUtils
import com.seeho.tilly.core.domain.GetAllTilsUseCase
import com.seeho.tilly.core.model.Emotion
import com.seeho.tilly.core.model.Til
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import javax.inject.Inject
import com.seeho.tilly.core.designsystem.util.color
import com.seeho.tilly.core.designsystem.theme.ChartPalette
import com.seeho.tilly.core.designsystem.theme.NeoSubtext

/**
 * Statistics 화면 ViewModel
 * 실제 데이터 연동
 */
@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getAllTilsUseCase: GetAllTilsUseCase,
) : ViewModel() {

    private val _currentMonthState = MutableStateFlow(CurrentMonthState())

    // DB에서 모든 TIL 데이터를 가져와 UI 상태로 매핑
    val uiState: StateFlow<StatisticsUiState> = combine(
        _currentMonthState,
        getAllTilsUseCase(),
    ) { monthState, tils ->
        val (month, year) = monthState

        // 이번 달 데이터 필터링
        val currentMonthTils = tils.filter {
            val date = DateUtils.timestampToLocalDate(it.createdAt)
            date.monthValue == month && date.year == year
        }

        // 해당 월의 총 일수 계산
        val daysInMonth = java.time.YearMonth.of(year, month).lengthOfMonth()

        StatisticsUiState(
            currentMonth = month,
            currentYear = year,
            tilCount = currentMonthTils.size,
            canGoNext = !isCurrentOrFuture(month, year),
            isLoading = false,
            
            // 감정 추세 (선택된 월 전체)
            emotionTrendData = mapToEmotionTrend(month, year, tils),
            daysInMonth = daysInMonth,
            
            // 학습 키워드 (이번 달)
            learningKeywords = mapToLearningKeywords(currentMonthTils),
            
            // 감정 분포 (이번 달)
            emotionDistribution = mapToEmotionDistribution(currentMonthTils),
            
            retrospectiveText = "${year}년 ${month}월 학습 회고",
            hasRetrospective = currentMonthTils.isNotEmpty(),
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = StatisticsUiState()
    )

    // 이전 달로 이동
    fun onPreviousMonth() {
        _currentMonthState.value = _currentMonthState.value.let { state ->
            if (state.month == 1) {
                state.copy(month = 12, year = state.year - 1)
            } else {
                state.copy(month = state.month - 1)
            }
        }
    }

    // 다음 달로 이동
    fun onNextMonth() {
        _currentMonthState.value = _currentMonthState.value.let { state ->
            if (state.month == 12) {
                state.copy(month = 1, year = state.year + 1)
            } else {
                state.copy(month = state.month + 1)
            }
        }
    }

    // 월간 회고 생성
    fun onGenerateRetrospective() {
        // TODO: AI 기반 월간 회고 생성 로직 구현
    }

    // 현재 월 또는 미래 월인지 확인
    private fun isCurrentOrFuture(month: Int, year: Int): Boolean {
        val now = LocalDate.now()
        return year > now.year || (year == now.year && month >= now.monthValue)
    }

    // TIL 데이터를 감정 추세 데이터로 변환 (일별 감정 점수)
    private fun mapToEmotionTrend(month: Int, year: Int, tils: List<Til>): List<EmotionTrendItem> {
        return tils
            .filter {
                val date = DateUtils.timestampToLocalDate(it.createdAt)
                date.monthValue == month && date.year == year
            }
            .map { til ->
                EmotionTrendItem(
                    day = DateUtils.timestampToLocalDate(til.createdAt).dayOfMonth,
                    score = (til.emotionScore ?: 3).toFloat()
                )
            }
            .sortedBy { it.day }
    }

    // TIL 태그를 학습 키워드 빈도 데이터로 변환
    private fun mapToLearningKeywords(tils: List<Til>): List<LearningKeywordItem> {
        val maxKeywords = 6

        val tagCounts = tils.flatMap { it.tags }
            .groupingBy { it }
            .eachCount()
            .entries
            .sortedByDescending { it.value }

        val totalCount = tagCounts.sumOf { it.value }.coerceAtLeast(1)

        val topKeywords = tagCounts.take(maxKeywords)
        val otherKeywords = tagCounts.drop(maxKeywords)

        val result = topKeywords.mapIndexed { index, (keyword, count) ->
            LearningKeywordItem(
                keyword = keyword,
                count = count.toFloat(),
                percent = (count * 100 / totalCount),
                color = ChartPalette[index % ChartPalette.size]
            )
        }.toMutableList()

        // 7개 이상인 경우 나머지를 "기타"로 합침
        if (otherKeywords.isNotEmpty()) {
            val othersCount = otherKeywords.sumOf { it.value }
            result.add(
                LearningKeywordItem(
                    keyword = "기타",
                    count = othersCount.toFloat(),
                    percent = (othersCount * 100 / totalCount),
                    color = NeoSubtext
                )
            )
        }

        return result
    }

    // TIL 감정 데이터를 감정 분포 데이터로 변환 (5개 감정 모두 표시)
    private fun mapToEmotionDistribution(tils: List<Til>): List<EmotionDistributionItem> {
        val emotionCounts = tils.mapNotNull { it.emotion }
            .groupingBy { it }
            .eachCount()

        return Emotion.entries.map { emotion ->
            EmotionDistributionItem(
                label = emotion.label,
                count = (emotionCounts[emotion] ?: 0).toFloat(),
                color = emotion.color
            )
        }
    }
}

data class CurrentMonthState(
    val month: Int = LocalDate.now().monthValue,
    val year: Int = LocalDate.now().year
)

