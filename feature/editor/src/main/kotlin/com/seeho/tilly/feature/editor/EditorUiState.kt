package com.seeho.tilly.feature.editor

import com.seeho.tilly.core.model.Difficulty
import com.seeho.tilly.core.model.Emotion

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
    // 기존 AI 분석 결과 캐싱 (수정 모드에서 보존용)
    val existingTags: List<String> = emptyList(),
    val existingEmotion: Emotion? = null,
    val existingEmotionScore: Int? = null,
    val existingDifficultyLevel: Difficulty? = null,
    val existingFeedback: String? = null,
    // 분석 횟수 및 코인 관련
    val remainingFreeAnalysis: Int = 3,  // 남은 무료 분석 횟수
    val coinBalance: Int = 0,            // 현재 코인 잔액
    val showPaidAnalysisDialog: Boolean = false, // 유료 분석 확인 다이얼로그
    val showOfflineSaveDialog: Boolean = false,  // 오프라인 저장 경고 다이얼로그
) {
    val isSaveEnabled: Boolean
        get() = title.isNotBlank() && todayLearning.isNotBlank() && !isSaving && !isAnalyzing
}

sealed interface EditorEvent {
    /** 저장 성공 → 상세 화면으로 이동 (tilId 전달) */
    data class SaveSuccess(val tilId: Long) : EditorEvent

    /** 저장 실패 → 에러 스낵바 표시 */
    data object SaveFailed : EditorEvent

    /** AI 분석 횟수 초과 + 코인 부족 */
    data object AnalysisLimitReached : EditorEvent

    /** 재분석 성공 → 스낵바 표시 */
    data object ReanalysisSuccess : EditorEvent
}
