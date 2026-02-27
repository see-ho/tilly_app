package com.seeho.tilly.core.model

/**
 * 유저 코인 정보 도메인 모델
 * 코인 잔액 + 일일 보상 플래그 + 스트릭 정보
 */
data class UserCoin(
    val balance: Int = 0,                   // 현재 코인 잔액
    val lastClaimedDate: String? = null,    // 마지막 보상 수령일 ("yyyy-MM-dd")
    val dailyTilClaimed: Boolean = false,   // 오늘 TIL 작성 보상 수령 여부
    val dailyAttendanceClaimed: Boolean = false, // 오늘 출석 보상 수령 여부
    val dailyAdWatchCount: Int = 0,         // 오늘 광고 시청 횟수
    val streakCount: Int = 0,               // 연속 작성 일수
    val dailyAnalysisCount: Int = 0,        // 오늘 AI 분석 사용 횟수
    val lastRetrospectiveMonth: String? = null, // 마지막 무료 회고 생성 월 ("yyyy-MM")
)
