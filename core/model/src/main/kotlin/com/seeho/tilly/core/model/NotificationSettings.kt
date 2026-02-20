package com.seeho.tilly.core.model

/**
 * 알림 설정 도메인 모델
 * 리마인더, 계획, 격려 알림 각각의 활성화 여부 + 시간 설정
 */
data class NotificationSettings(
    val reminderEnabled: Boolean = false,   // TIL 작성 리마인더
    val reminderHour: Int = 20,             // 리마인더 시간 (기본 20시)
    val reminderMinute: Int = 0,            // 리마인더 분

    val planEnabled: Boolean = false,       // 계획 알람
    val planHour: Int = 9,                  // 계획 알람 시간 (기본 09시)
    val planMinute: Int = 0,                // 계획 알람 분

)
