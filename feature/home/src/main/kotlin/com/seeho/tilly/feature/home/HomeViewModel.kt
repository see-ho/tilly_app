package com.seeho.tilly.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seeho.tilly.core.domain.GetAllTilsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getAllTilsUseCase: GetAllTilsUseCase,
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
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState.Loading,
        )
}
