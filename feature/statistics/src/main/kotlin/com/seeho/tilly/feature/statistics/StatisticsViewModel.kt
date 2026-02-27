package com.seeho.tilly.feature.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seeho.tilly.core.common.util.DateUtils
import com.seeho.tilly.core.domain.GenerateRetrospectiveUseCase
import com.seeho.tilly.core.domain.GetAllTilsUseCase
import com.seeho.tilly.core.domain.repository.AiAnalysisRepository
import com.seeho.tilly.core.domain.repository.CoinRepository
import com.seeho.tilly.core.model.Difficulty
import com.seeho.tilly.core.model.Emotion
import com.seeho.tilly.core.model.Til
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import com.seeho.tilly.core.designsystem.theme.ChartPalette
import com.seeho.tilly.core.designsystem.theme.NeoSubtext
import com.seeho.tilly.core.designsystem.util.color

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getAllTilsUseCase: GetAllTilsUseCase,
    private val generateRetrospectiveUseCase: GenerateRetrospectiveUseCase,
    private val aiAnalysisRepository: AiAnalysisRepository,
    private val coinRepository: CoinRepository,
) : ViewModel() {

    companion object {
        /** 월간 회고 생성에 필요한 최소 TIL 수 */
        const val MIN_TILS_FOR_RETROSPECTIVE = 5
    }

    private val _currentMonthState = MutableStateFlow(CurrentMonthState())

    // 회고 로딩/에러 상태 (combine 외부에서 관리)
    private val _retrospectiveLoadingState = MutableStateFlow(RetrospectiveLoadingState())

    // 월이 바뀔 때마다 해당 월의 회고를 Room에서 조회
    private val retrospectiveFlow = _currentMonthState.flatMapLatest { state ->
        aiAnalysisRepository.getRetrospective(state.month, state.year)
    }

    val uiState: StateFlow<StatisticsUiState> = combine(
        _currentMonthState,
        getAllTilsUseCase(),
        retrospectiveFlow,
        _retrospectiveLoadingState,
    ) { monthState, tils, retrospective, loadingState ->
        val (month, year) = monthState

        val currentMonthTils = tils.filter {
            val date = DateUtils.timestampToLocalDate(it.createdAt)
            date.monthValue == month && date.year == year
        }

        val daysInMonth = java.time.YearMonth.of(year, month).lengthOfMonth()

        StatisticsUiState(
            currentMonth = month,
            currentYear = year,
            tilCount = currentMonthTils.size,
            canGoNext = !isCurrentOrFuture(month, year),
            isLoading = false,
            
            // 감정 추세 (이번 달)
            emotionTrendData = mapToEmotionTrend(currentMonthTils),
            daysInMonth = daysInMonth,
            
            // 학습 키워드 (이번 달)
            learningKeywords = mapToLearningKeywords(currentMonthTils),
            
            // 감정 분포 (이번 달)
            emotionDistribution = mapToEmotionDistribution(currentMonthTils),

            difficultyDistribution = mapToDifficultyDistribution(currentMonthTils),

            // 평균 감정 점수 계산
            averageEmotionScore = currentMonthTils
                .mapNotNull { it.emotionScore }
                .takeIf { it.isNotEmpty() }
                ?.average()?.toFloat() ?: 0f,

            retrospective = retrospective,
            isRetrospectiveLoading = loadingState.isLoading,
            retrospectiveError = loadingState.error,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = StatisticsUiState()
    )

    fun onPreviousMonth() {
        _currentMonthState.value = _currentMonthState.value.let { state ->
            if (state.month == 1) {
                state.copy(month = 12, year = state.year - 1)
            } else {
                state.copy(month = state.month - 1)
            }
        }
    }

    fun onNextMonth() {
        _currentMonthState.value = _currentMonthState.value.let { state ->
            if (state.month == 12) {
                state.copy(month = 1, year = state.year + 1)
            } else {
                state.copy(month = state.month + 1)
            }
        }
    }

    // 월간 회고 생성 (GPT 호출) — 월 1회 무료, 이후 코인 차감
    fun onGenerateRetrospective() {
        val state = _currentMonthState.value
        viewModelScope.launch {
            // 1. TIL 개수 검증을 먼저 수행 (코인 차감 방지)
            val currentTils = getAllTilsUseCase().first().filter { til ->
                val date = DateUtils.timestampToLocalDate(til.createdAt)
                date.monthValue == state.month && date.year == state.year
            }

            if (currentTils.size < MIN_TILS_FOR_RETROSPECTIVE) {
                _retrospectiveLoadingState.update {
                    it.copy(isLoading = false, error = "TIL ${MIN_TILS_FOR_RETROSPECTIVE}개 이상 작성해야 회고를 생성할 수 있어요 (현재 ${currentTils.size}개)")
                }
                return@launch
            }

            // 2. TIL 검증 통과 후 코인 차감
            val consumed = coinRepository.consumeRetrospective(state.month, state.year)
            if (!consumed) {
                _retrospectiveLoadingState.update {
                    it.copy(isLoading = false, error = "코인이 부족해요. 회고 재생성에는 50코인이 필요해요")
                }
                return@launch
            }

            _retrospectiveLoadingState.update { it.copy(isLoading = true, error = null) }

            generateRetrospectiveUseCase(state.month, state.year, currentTils)
                .onSuccess {
                    _retrospectiveLoadingState.update { it.copy(isLoading = false, error = null) }
                }
                .onFailure { e ->
                    _retrospectiveLoadingState.update {
                        it.copy(isLoading = false, error = e.message ?: "회고 생성에 실패했어요")
                    }
                }
        }
    }

    private fun isCurrentOrFuture(month: Int, year: Int): Boolean {
        val now = LocalDate.now()
        return year > now.year || (year == now.year && month >= now.monthValue)
    }

    private fun mapToEmotionTrend(tils: List<Til>): List<EmotionTrendItem> {
        return tils
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

    // TIL 난이도 데이터를 난이도 분포 데이터로 변환
    private fun mapToDifficultyDistribution(tils: List<Til>): List<DifficultyDistributionItem> {
        val difficultyCounts = tils
            .map { it.difficultyLevel ?: Difficulty.NORMAL }
            .groupingBy { it }
            .eachCount()

        return Difficulty.entries.map { difficulty ->
            DifficultyDistributionItem(
                difficulty = difficulty,
                count = difficultyCounts[difficulty] ?: 0,
                color = difficulty.color,
            )
        }
    }
}

data class CurrentMonthState(
    val month: Int = LocalDate.now().monthValue,
    val year: Int = LocalDate.now().year
)

data class RetrospectiveLoadingState(
    val isLoading: Boolean = false,
    val error: String? = null,
)
