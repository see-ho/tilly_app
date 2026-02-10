package com.seeho.tilly.feature.editor

/**
 * Editor 화면의 UI 상태
 */
data class EditorUiState(
    val title: String = "",
    val todayLearning: String = "",
    val difficulties: String = "",
    val tomorrowPlan: String = "",
    val isEditMode: Boolean = false,     // true: 수정 모드, false: 생성 모드
    val isSaving: Boolean = false,       // 저장 중 여부
    val isLoading: Boolean = false,      // 기존 TIL 로딩 중 여부
) {
    val isSaveEnabled: Boolean
        get() = title.isNotBlank() && todayLearning.isNotBlank() && !isSaving
}

sealed interface EditorEvent {
    /** 저장 성공 → 화면 종료 */
    data object SaveSuccess : EditorEvent
}
