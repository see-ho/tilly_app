package com.seeho.tilly.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seeho.tilly.core.domain.DeleteTilUseCase
import com.seeho.tilly.core.domain.GetAllTilsUseCase
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class HomeViewModel @Inject constructor(
    getAllTilsUseCase: GetAllTilsUseCase,
    private val deleteTilUseCase: DeleteTilUseCase,
) : ViewModel() {

    /**
     * Home 화면 UI 상태
     * Flow를 StateFlow로 변환하여 Compose에서 수집
     * WhileSubscribed(5_000): 구독자가 없어도 5초간 캐싱 (화면 회전 등에 유리)
     */
    val uiState: StateFlow<HomeUiState> = getAllTilsUseCase()
        .map { tils ->
            if (tils.isEmpty()) {
                HomeUiState.Empty
            } else {
                HomeUiState.Success(tils)
            }
        }
        .catch { e ->
            emit(HomeUiState.Error(e.message))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState.Loading,
        )

    /** 삭제할 TIL ID (null이면 다이얼로그 미표시) */
    private val _deletingTilId = MutableStateFlow<Long?>(null)
    val deletingTilId: StateFlow<Long?> = _deletingTilId.asStateFlow()

    /** 삭제 다이얼로그 표시 */
    fun showDeleteDialog(id: Long) {
        _deletingTilId.value = id
    }

    /** 삭제 다이얼로그 숨기기 */
    fun dismissDeleteDialog() {
        _deletingTilId.value = null
    }

    /** 삭제 확정 */
    fun deleteTil() {
        val id = _deletingTilId.value ?: return
        viewModelScope.launch {
            try {
                deleteTilUseCase(id)
                dismissDeleteDialog()
            } catch (e: Exception) {
                // 에러 처리는 UI State에서 Error로 전파되거나 별도 이벤트로 처리
                e.printStackTrace()
            }
        }
    }
}
