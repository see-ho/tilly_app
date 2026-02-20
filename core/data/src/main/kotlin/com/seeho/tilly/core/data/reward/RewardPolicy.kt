package com.seeho.tilly.core.data.reward

import com.seeho.tilly.core.model.RewardItem

/**
 * 보상 정책 (금액·설명 상수 + 스트릭 마일스톤 판정 로직)
 * 보상 금액이나 마일스톤 변경 시 이 파일만 수정
 */
object RewardPolicy {

    // 보상 금액 상수
    const val ATTENDANCE_REWARD = 5       // 출석 보상
    const val TIL_REWARD = 20            // TIL 작성 보상

    // 스트릭 보너스 금액
    private const val STREAK_3_BONUS = 10
    private const val STREAK_7_BONUS = 30
    private const val STREAK_30_BONUS = 100

    // 보상 설명 상수
    const val ATTENDANCE_DESC = "출석 보상"
    const val TIL_REWARD_DESC = "TIL 작성 보상"

    /**
     * 스트릭 보너스 확인
     * 해당하는 마일스톤이면 [RewardItem] 반환, 아니면 null
     */
    fun getStreakBonus(streak: Int): RewardItem? {
        return when (streak) {
            3 -> RewardItem(amount = STREAK_3_BONUS, description = "3일 연속 학습 보너스")
            7 -> RewardItem(amount = STREAK_7_BONUS, description = "7일 연속 학습 보너스")
            30 -> RewardItem(amount = STREAK_30_BONUS, description = "30일 연속 학습 보너스")
            else -> null
        }
    }

    /** 출석 보상 RewardItem 생성 */
    fun attendanceReward(): RewardItem = RewardItem(
        amount = ATTENDANCE_REWARD,
        description = ATTENDANCE_DESC,
    )

    /** TIL 작성 보상 RewardItem 생성 */
    fun tilReward(): RewardItem = RewardItem(
        amount = TIL_REWARD,
        description = TIL_REWARD_DESC,
    )
}
