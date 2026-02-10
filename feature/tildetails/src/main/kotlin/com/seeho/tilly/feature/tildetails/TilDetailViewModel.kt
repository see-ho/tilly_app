package com.seeho.tilly.feature.tildetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seeho.tilly.core.domain.DeleteTilUseCase
import com.seeho.tilly.core.domain.GetTilByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TilDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getTilByIdUseCase: GetTilByIdUseCase,
    private val deleteTilUseCase: DeleteTilUseCase,
) : ViewModel() {

    // Navigation 인자에서 tilId 추출
    private val tilId: Long = checkNotNull(savedStateHandle.get<Long>("tilId")) {
        "tilId는 반드시 전달되어야 합니다"
    }

    /**
     * Detail 화면 UI 상태
     */
    val uiState: StateFlow<TilDetailUiState> = getTilByIdUseCase(tilId)
        .map { til ->
            if (til != null) {
                TilDetailUiState.Success(til)
            } else {
                TilDetailUiState.NotFound
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TilDetailUiState.Loading,
        )

    private val _event = MutableSharedFlow<TilDetailEvent>()
    val event: SharedFlow<TilDetailEvent> = _event.asSharedFlow()

    /** TIL 삭제 */
    fun onDelete() {
        viewModelScope.launch {
            try {
                deleteTilUseCase(tilId)
                _event.emit(TilDetailEvent.DeleteSuccess)
            } catch (e: Exception) {
                // TODO 에러 처리
                e.printStackTrace()
                _event.emit(TilDetailEvent.DeleteFailed)
            }
        }
    }
}
