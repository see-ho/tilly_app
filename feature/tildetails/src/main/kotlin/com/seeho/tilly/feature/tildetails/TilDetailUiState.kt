package com.seeho.tilly.feature.tildetails

import com.seeho.tilly.core.model.Til

/**
 * Detail 화면의 UI 상태
 */
sealed interface TilDetailUiState {
    data object Loading : TilDetailUiState
    data class Success(val til: Til) : TilDetailUiState
    /** TIL을 찾을 수 없음 (삭제되었거나 잘못된 ID) */
    data object NotFound : TilDetailUiState
}

sealed interface TilDetailEvent {
    /** 삭제 성공 → 화면 종료 */
    data object DeleteSuccess : TilDetailEvent
}
