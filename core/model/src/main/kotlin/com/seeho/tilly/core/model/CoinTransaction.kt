package com.seeho.tilly.core.model

import java.time.LocalDateTime

data class CoinTransaction(
    val id: Long,
    val amount: Int,
    val type: CoinTransactionType,
    val description: String,
    val balanceAfter: Int,              // 거래 후 잔액
    val createdAt: LocalDateTime,
)

enum class CoinTransactionType {
    TIL_REWARD,      // TIL 작성 보상
    ATTENDANCE,      // 출석 보상
    STREAK_BONUS,    // 연속 학습 보너스
    PURCHASE,        // 아이템 구매
    AD_REWARD,       // 광고 시청 보상
    UNKNOWN,         // 알 수 없는 타입 (폴백용)
}
