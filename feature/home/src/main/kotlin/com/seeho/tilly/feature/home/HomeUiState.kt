package com.seeho.tilly.feature.home

import com.seeho.tilly.core.model.Til

/**
 * Home 화면의 UI 상태
 */
sealed interface HomeUiState {
    /** 로딩 중 */
    data object Loading : HomeUiState

    /** TIL 목록이 비어있을 때 */
    data object Empty : HomeUiState

    /** TIL 목록 로드 성공 */
    data class Success(val tils: List<Til>) : HomeUiState

    /** 에러 발생 */
    data class Error(val message: String?) : HomeUiState
}
