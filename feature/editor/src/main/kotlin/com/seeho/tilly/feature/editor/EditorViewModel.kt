package com.seeho.tilly.feature.editor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seeho.tilly.core.domain.GetTilByIdUseCase
import com.seeho.tilly.core.domain.SaveTilUseCase
import com.seeho.tilly.core.domain.UpdateTilUseCase
import com.seeho.tilly.core.domain.AnalyzeTilUseCase
import com.seeho.tilly.core.domain.ClaimTilRewardUseCase
import com.seeho.tilly.core.domain.repository.CoinRepository
import com.seeho.tilly.core.common.util.NetworkMonitor
import com.seeho.tilly.core.common.widget.WidgetUpdater
import com.seeho.tilly.core.model.RewardResult
import com.seeho.tilly.core.model.Til
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import com.seeho.tilly.core.model.Difficulty
import com.seeho.tilly.core.model.Emotion

@HiltViewModel
class EditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val saveTilUseCase: SaveTilUseCase,
    private val updateTilUseCase: UpdateTilUseCase,
    private val getTilByIdUseCase: GetTilByIdUseCase,
    private val analyzeTilUseCase: AnalyzeTilUseCase,
    private val claimTilRewardUseCase: ClaimTilRewardUseCase,
    private val coinRepository: CoinRepository,
    private val networkMonitor: NetworkMonitor,
    private val widgetUpdater: WidgetUpdater,
) : ViewModel() {

    // Navigation 인자에서 tilId 추출 (null이면 생성 모드)
    private val tilId: Long? = savedStateHandle.get<Long>("tilId")?.takeIf { it != -1L }

    private val _uiState = MutableStateFlow(EditorUiState(isEditMode = tilId != null))
    val uiState: StateFlow<EditorUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<EditorEvent>()
    val event: SharedFlow<EditorEvent> = _event.asSharedFlow()

    // 코인 보상 이벤트 — 보상 다이얼로그 표시용
    private val _coinRewardEvent = MutableStateFlow<RewardResult?>(null)
    val coinRewardEvent: StateFlow<RewardResult?> = _coinRewardEvent.asStateFlow()

    // 저장 성공 시 네비게이션할 TIL ID (보상 다이얼로그 닫힌 후 사용)
    private val _pendingNavigationId = MutableStateFlow<Long?>(null)
    val pendingNavigationId: StateFlow<Long?> = _pendingNavigationId.asStateFlow()

    init {
        // 수정 모드: 기존 TIL 데이터 로딩
        if (tilId != null) {
            loadTil(tilId)
        }
        // 분석 횟수 및 잔액 로드
        loadAnalysisInfo()
    }

    /** 남은 분석 횟수 + 코인 잔액 로드 */
    private fun loadAnalysisInfo() {
        viewModelScope.launch {
            val remaining = coinRepository.getRemainingFreeAnalysis()
            val balance = coinRepository.getBalance()
            _uiState.update {
                it.copy(
                    remainingFreeAnalysis = remaining,
                    coinBalance = balance,
                )
            }
        }
    }

    /** 기존 TIL 데이터를 불러와 입력 필드에 채움 */
    private fun loadTil(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val til = getTilByIdUseCase(id).firstOrNull()
            if (til != null) {
                _uiState.update {
                    it.copy(
                        title = til.title,
                        todayLearning = til.learned,
                        difficulties = til.difficulty ?: "",
                        tomorrowPlan = til.tomorrow ?: "",
                        isLoading = false,
                        createdAt = til.createdAt,
                        // 기존 분석 결과 캐싱 (수정 시 보존용)
                        existingTags = til.tags,
                        existingEmotion = til.emotion,
                        existingEmotionScore = til.emotionScore,
                        existingDifficultyLevel = til.difficultyLevel,
                        existingFeedback = til.feedback,
                    )
                }
            } else {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    // 입력 값 변경 핸들러들
    fun onTitleChange(value: String) {
        _uiState.update { it.copy(title = value) }
    }

    fun onTodayLearningChange(value: String) {
        _uiState.update { it.copy(todayLearning = value) }
    }

    fun onDifficultiesChange(value: String) {
        _uiState.update { it.copy(difficulties = value) }
    }

    fun onTomorrowPlanChange(value: String) {
        _uiState.update { it.copy(tomorrowPlan = value) }
    }

    /** TIL 저장 (생성 또는 수정) */
    fun onSave() {
        val state = _uiState.value
        if (!state.isSaveEnabled) return

        viewModelScope.launch {
            // 생성 모드에서 오프라인이면 경고 다이얼로그 표시
            if (!state.isEditMode && !networkMonitor.isOnline()) {
                _uiState.update { it.copy(showOfflineSaveDialog = true) }
                return@launch
            }

            // 수정 모드: AI 분석 없이 텍스트만 저장 (기존 분석 결과 유지)
            // 생성 모드: AI 분석 후 저장 (횟수 소비)
            val analysisResult = if (!state.isEditMode) {
                _uiState.update { it.copy(isAnalyzing = true) }
                // 분석 횟수 소비 시도
                val consumed = coinRepository.consumeAnalysis()
                if (!consumed) {
                    _uiState.update { it.copy(isAnalyzing = false) }
                    _event.emit(EditorEvent.AnalysisLimitReached)
                    return@launch
                }
                try {
                    analyzeTilUseCase(
                        title = state.title,
                        learned = state.todayLearning,
                        difficulty = state.difficulties.ifBlank { null },
                        tomorrow = state.tomorrowPlan.ifBlank { null }
                    ).getOrNull()
                } catch (e: CancellationException) {
                    throw e
                } finally {
                    _uiState.update { it.copy(isAnalyzing = false) }
                }
            } else {
                null // 수정 모드: 분석 스킵
            }

            _uiState.update { it.copy(isSaving = true) }

            try {
                // 수정 모드이면 기존 createdAt 사용, 없으면 현재 시간
                val createdAt = state.createdAt ?: System.currentTimeMillis()

                val til = buildTilFromState(
                    state = state,
                    tags = analysisResult?.tags ?: state.existingTags,
                    emotion = analysisResult?.emotion ?: state.existingEmotion,
                    emotionScore = analysisResult?.emotionScore ?: state.existingEmotionScore,
                    difficultyLevel = analysisResult?.difficultyLevel ?: state.existingDifficultyLevel,
                    feedback = analysisResult?.feedback ?: state.existingFeedback,
                )

                val savedId = if (tilId != null) {
                    updateTilUseCase(til)
                    tilId
                } else {
                    saveTilUseCase(til)
                }

                // 위젯 즉시 갱신 (스트릭 + 주간 체크)
                try { widgetUpdater.updateAll() } catch (_: Exception) {}

                // 새 TIL 작성 시에만 코인 보상 지급 (수정 모드 제외)
                if (tilId == null) {
                    try {
                        val rewardResult = claimTilRewardUseCase()
                        if (rewardResult != null) {
                            // 보상 수령 성공 → 다이얼로그 표시 후 네비게이션
                            _pendingNavigationId.value = savedId
                            _coinRewardEvent.value = rewardResult
                            return@launch // 네비게이션은 다이얼로그 닫힌 후
                        }
                    } catch (_: Exception) {
                        // 코인 지급 실패해도 TIL 저장은 성공으로 처리
                    }
                }

                _event.emit(EditorEvent.SaveSuccess(savedId))
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                e.printStackTrace()
                _event.emit(EditorEvent.SaveFailed)
            } finally {
                _uiState.update { it.copy(isSaving = false) }
            }
        }
    }

    /** 보상 다이얼로그 닫기 → 대기 중인 네비게이션 실행 */
    fun consumeCoinRewardEvent() {
        _coinRewardEvent.value = null
        val navId = _pendingNavigationId.value
        if (navId != null) {
            _pendingNavigationId.value = null
            viewModelScope.launch {
                _event.emit(EditorEvent.SaveSuccess(navId))
            }
        }
    }

    /** 재분석 버튼 클릭: 무료 횟수 남으면 바로 실행, 없으면 유료 확인 다이얼로그 */
    fun onReanalyzeClick() {
        val state = _uiState.value
        if (state.remainingFreeAnalysis > 0) {
            // 무료 횟수 남음 → 바로 재분석
            executeReanalysis()
        } else {
            // 유료 → 확인 다이얼로그 표시
            _uiState.update { it.copy(showPaidAnalysisDialog = true) }
        }
    }

    /** 유료 분석 확인 다이얼로그에서 확인 클릭 */
    fun confirmPaidAnalysis() {
        _uiState.update { it.copy(showPaidAnalysisDialog = false) }
        executeReanalysis()
    }

    /** 유료 분석 확인 다이얼로그에서 취소 클릭 */
    fun dismissPaidAnalysisDialog() {
        _uiState.update { it.copy(showPaidAnalysisDialog = false) }
    }

    /** 오프라인 저장 확인 → AI 분석/차감 없이 바로 저장 (코인 보상은 지급) */
    fun confirmOfflineSave() {
        _uiState.update { it.copy(showOfflineSaveDialog = false) }
        val state = _uiState.value

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            try {
                val til = buildTilFromState(
                    state = state,
                    // AI 분석 결과 없음 (오프라인)
                    tags = emptyList(),
                    emotion = null,
                    emotionScore = null,
                    difficultyLevel = null,
                    feedback = null,
                )

                val savedId = saveTilUseCase(til)

                // 위젯 즉시 갱신
                try { widgetUpdater.updateAll() } catch (_: Exception) {}

                // TIL 작성 보상은 오프라인에서도 지급
                try {
                    val rewardResult = claimTilRewardUseCase()
                    if (rewardResult != null) {
                        _pendingNavigationId.value = savedId
                        _coinRewardEvent.value = rewardResult
                        return@launch
                    }
                } catch (e: Exception) {
                    // 코인 지급 실패해도 TIL 저장은 성공으로 처리
                    e.printStackTrace()
                }

                _event.emit(EditorEvent.SaveSuccess(savedId))
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                e.printStackTrace()
                _event.emit(EditorEvent.SaveFailed)
            } finally {
                _uiState.update { it.copy(isSaving = false) }
            }
        }
    }

    /** 오프라인 저장 다이얼로그 닫기 */
    fun dismissOfflineSaveDialog() {
        _uiState.update { it.copy(showOfflineSaveDialog = false) }
    }

    /** 실제 재분석 실행 (무료/유료 공통) → 성공 시 자동 저장 + 디테일 이동 */
    private fun executeReanalysis() {
        val state = _uiState.value
        viewModelScope.launch {
            // 네트워크 확인
            if (!networkMonitor.isOnline()) {
                _event.emit(EditorEvent.SaveFailed)
                return@launch
            }

            _uiState.update { it.copy(isAnalyzing = true) }
            try {
                // 1. 먼저 AI 분석 실행 (네트워크 호출)
                val result = analyzeTilUseCase(
                    title = state.title,
                    learned = state.todayLearning,
                    difficulty = state.difficulties.ifBlank { null },
                    tomorrow = state.tomorrowPlan.ifBlank { null }
                ).getOrNull()

                if (result != null) {
                    // 2. 분석 성공 후에 횟수/코인 차감 (실패 시 코인 보호)
                    val consumed = coinRepository.consumeAnalysis()
                    if (!consumed) {
                        _uiState.update { it.copy(isAnalyzing = false) }
                        _event.emit(EditorEvent.AnalysisLimitReached)
                        return@launch
                    }

                    // 3. 분석 결과 캐싱
                    _uiState.update {
                        it.copy(
                            existingTags = result.tags,
                            existingEmotion = result.emotion,
                            existingEmotionScore = result.emotionScore,
                            existingDifficultyLevel = result.difficultyLevel,
                            existingFeedback = result.feedback,
                        )
                    }
                    _uiState.update { it.copy(isAnalyzing = false) }

                    // 4. 재분석 성공 → 자동 저장 + 디테일 이동
                    saveAfterReanalysis()
                } else {
                    _uiState.update { it.copy(isAnalyzing = false) }
                    _event.emit(EditorEvent.SaveFailed)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (_: Exception) {
                _uiState.update { it.copy(isAnalyzing = false) }
                _event.emit(EditorEvent.SaveFailed)
            } finally {
                loadAnalysisInfo()
            }
        }
    }

    /** 재분석 후 자동 저장 — 수정 모드에서 현재 상태로 업데이트 저장 */
    private suspend fun saveAfterReanalysis() {
        val state = _uiState.value
        _uiState.update { it.copy(isSaving = true) }
        try {
            val til = buildTilFromState(
                state = state,
                tags = state.existingTags,
                emotion = state.existingEmotion,
                emotionScore = state.existingEmotionScore,
                difficultyLevel = state.existingDifficultyLevel,
                feedback = state.existingFeedback,
            )
            if (tilId != null) {
                updateTilUseCase(til)
                // 위젯 즉시 갱신
                try { widgetUpdater.updateAll() } catch (_: Exception) {}
                _event.emit(EditorEvent.SaveSuccess(tilId))
            }
        } catch (e: CancellationException) {
            throw e
        } catch (_: Exception) {
            _event.emit(EditorEvent.SaveFailed)
        } finally {
            _uiState.update { it.copy(isSaving = false) }
        }
    }

    /**
     * 현재 UI 상태로부터 Til 객체를 생성하는 헬퍼 함수
     */
    private fun buildTilFromState(
        state: EditorUiState,
        tags: List<String> = state.existingTags,
        emotion: Emotion? = state.existingEmotion,
        emotionScore: Int? = state.existingEmotionScore,
        difficultyLevel: Difficulty? = state.existingDifficultyLevel,
        feedback: String? = state.existingFeedback,
    ): Til = Til(
        id = tilId ?: 0L,
        title = state.title,
        learned = state.todayLearning,
        difficulty = state.difficulties.ifBlank { null },
        tomorrow = state.tomorrowPlan.ifBlank { null },
        tags = tags,
        emotion = emotion,
        emotionScore = emotionScore,
        difficultyLevel = difficultyLevel,
        feedback = feedback,
        createdAt = state.createdAt ?: System.currentTimeMillis(),
        updatedAt = if (tilId != null) System.currentTimeMillis() else null,
    )
}
