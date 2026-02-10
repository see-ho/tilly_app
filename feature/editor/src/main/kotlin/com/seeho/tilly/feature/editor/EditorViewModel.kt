package com.seeho.tilly.feature.editor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seeho.tilly.core.domain.GetTilByIdUseCase
import com.seeho.tilly.core.domain.SaveTilUseCase
import com.seeho.tilly.core.domain.UpdateTilUseCase
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

@HiltViewModel
class EditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val saveTilUseCase: SaveTilUseCase,
    private val updateTilUseCase: UpdateTilUseCase,
    private val getTilByIdUseCase: GetTilByIdUseCase,
) : ViewModel() {

    // Navigation 인자에서 tilId 추출 (null이면 생성 모드)
    private val tilId: Long? = savedStateHandle.get<Long>("tilId")?.takeIf { it != -1L }

    private val _uiState = MutableStateFlow(EditorUiState(isEditMode = tilId != null))
    val uiState: StateFlow<EditorUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<EditorEvent>()
    val event: SharedFlow<EditorEvent> = _event.asSharedFlow()

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
            _uiState.update { it.copy(isSaving = true) }

            val til = Til(
                id = tilId ?: 0L,
                title = state.title,
                learned = state.todayLearning,
                difficulty = state.difficulties.ifBlank { null },
                tomorrow = state.tomorrowPlan.ifBlank { null },
                tags = emptyList(),
                createdAt = if (tilId != null) 0L else System.currentTimeMillis(),
                updatedAt = if (tilId != null) System.currentTimeMillis() else null,
            )

            if (tilId != null) {
                // 수정 모드: 기존 createdAt 유지 필요
                val existingTil = getTilByIdUseCase(tilId).firstOrNull()
                val updatedTil = til.copy(
                    createdAt = existingTil?.createdAt ?: System.currentTimeMillis(),
                )
                updateTilUseCase(updatedTil)
            } else {
                // 생성 모드
                saveTilUseCase(til)
            }

            _uiState.update { it.copy(isSaving = false) }
            _event.emit(EditorEvent.SaveSuccess)
        }
    }
}
