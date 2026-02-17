package com.seeho.tilly.feature.editor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seeho.tilly.core.domain.GetTilByIdUseCase
import com.seeho.tilly.core.domain.SaveTilUseCase
import com.seeho.tilly.core.domain.UpdateTilUseCase
import com.seeho.tilly.core.domain.AnalyzeTilUseCase
import com.seeho.tilly.core.domain.ClaimTilRewardUseCase
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

@HiltViewModel
class EditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val saveTilUseCase: SaveTilUseCase,
    private val updateTilUseCase: UpdateTilUseCase,
    private val getTilByIdUseCase: GetTilByIdUseCase,
    private val analyzeTilUseCase: AnalyzeTilUseCase,
    private val claimTilRewardUseCase: ClaimTilRewardUseCase,
) : ViewModel() {

    // Navigation 인자에서 tilId 추출 (null이면 생성 모드)
    private val tilId: Long? = savedStateHandle.get<Long>("tilId")?.takeIf { it != -1L }

    private val _uiState = MutableStateFlow(EditorUiState(isEditMode = tilId != null))
    val uiState: StateFlow<EditorUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<EditorEvent>()
    val event: SharedFlow<EditorEvent> = _event.asSharedFlow()

    // 코인 보상 이벤트 (amount, reason) — 보상 다이얼로그 표시용
    private val _coinRewardEvent = MutableStateFlow<Pair<Int, String>?>(null)
    val coinRewardEvent: StateFlow<Pair<Int, String>?> = _coinRewardEvent.asStateFlow()

    // 저장 성공 시 네비게이션할 TIL ID (보상 다이얼로그 닫힌 후 사용)
    private val _pendingNavigationId = MutableStateFlow<Long?>(null)
    val pendingNavigationId: StateFlow<Long?> = _pendingNavigationId.asStateFlow()

    init {
        // 수정 모드: 기존 TIL 데이터 로딩
        if (tilId != null) {
            loadTil(tilId)
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
                        createdAt = til.createdAt
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
            _uiState.update { it.copy(isAnalyzing = true) }

            // AI 분석 — Result로 성공/실패 구분
            val analysisResult = try {
                analyzeTilUseCase(
                    title = state.title,
                    learned = state.todayLearning,
                    difficulty = state.difficulties.ifBlank { null },
                    tomorrow = state.tomorrowPlan.ifBlank { null }
                ).getOrNull() // 실패 시 null → TIL 자체는 분석 없이 저장
            } catch (e: CancellationException) {
                throw e
            } finally {
                _uiState.update { it.copy(isAnalyzing = false, isSaving = true) }
            }

            try {
                // 수정 모드이면 기존 createdAt 사용, 없으면 현재 시간
                val createdAt = state.createdAt ?: System.currentTimeMillis()

                val til = Til(
                    id = tilId ?: 0L,
                    title = state.title,
                    learned = state.todayLearning,
                    difficulty = state.difficulties.ifBlank { null },
                    tomorrow = state.tomorrowPlan.ifBlank { null },
                    tags = analysisResult?.tags ?: emptyList(),
                    emotion = analysisResult?.emotion,
                    emotionScore = analysisResult?.emotionScore,
                    difficultyLevel = analysisResult?.difficultyLevel,
                    feedback = analysisResult?.feedback,
                    createdAt = createdAt,
                    updatedAt = if (tilId != null) System.currentTimeMillis() else null,
                )

                val savedId = if (tilId != null) {
                    updateTilUseCase(til)
                    tilId
                } else {
                    saveTilUseCase(til)
                }

                // 새 TIL 작성 시에만 코인 보상 지급 (수정 모드 제외)
                if (tilId == null) {
                    try {
                        val claimed = claimTilRewardUseCase()
                        if (claimed) {
                            // 보상 수령 성공 → 다이얼로그 표시 후 네비게이션
                            _pendingNavigationId.value = savedId
                            _coinRewardEvent.value = 20 to "TIL 작성 보상"
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
}
