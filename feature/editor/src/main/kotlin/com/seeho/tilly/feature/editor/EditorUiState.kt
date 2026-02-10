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
    val isAnalyzing: Boolean = false,    // AI 분석 중 여부
    val isLoading: Boolean = false,      // 기존 TIL 로딩 중 여부
    val createdAt: Long? = null,         // 기존 생성일시 (수정 모드용)
) {
    val isSaveEnabled: Boolean
        get() = title.isNotBlank() && todayLearning.isNotBlank() && !isSaving && !isAnalyzing
}

sealed interface EditorEvent {
    /** 저장 성공 → 화면 종료 */
    /** 저장 성공 → 화면 종료 (tilId 전달) */
    data class SaveSuccess(val tilId: Long) : EditorEvent
}
