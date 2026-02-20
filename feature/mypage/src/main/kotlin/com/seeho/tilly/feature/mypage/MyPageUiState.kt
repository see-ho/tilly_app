package com.seeho.tilly.feature.mypage

import com.seeho.tilly.core.model.NotificationSettings

/**
 * 마이페이지 UI 상태
 */
data class MyPageUiState(
    val weeklyCheck: List<Boolean> = List(7) { false },  // 월~일 TIL 작성 여부
    val streakCount: Int = 0,                             // 연속 작성 일수
    val totalTilCount: Int = 0,                           // 총 TIL 작성 수
    val notificationSettings: NotificationSettings = NotificationSettings(),
    val appVersion: String = "",                          // 앱 버전
)
