package com.seeho.tilly.core.domain.repository

import com.seeho.tilly.core.model.UserCoin
import kotlinx.coroutines.flow.Flow

/**
 * 코인 Repository 인터페이스
 * 코인 잔액 관리 + 일일 보상 + 스트릭 관리
 */
interface CoinRepository {

    /** 유저 코인 정보 실시간 조회 */
    fun getUserCoin(): Flow<UserCoin>

    /** 출석 보상 지급 (성공 시 true) */
    suspend fun claimAttendance(): Boolean

    /** TIL 작성 보상 지급 (성공 시 true) */
    suspend fun claimTilReward(): Boolean

    /** 코인 추가 (범용) */
    suspend fun addCoins(amount: Int)

    /** 코인 차감 (잔액 부족 시 false) */
    suspend fun deductCoins(amount: Int): Boolean

    /** 일일 플래그 리셋 (날짜 변경 감지 시 호출) */
    suspend fun resetDailyFlagsIfNeeded()
}
