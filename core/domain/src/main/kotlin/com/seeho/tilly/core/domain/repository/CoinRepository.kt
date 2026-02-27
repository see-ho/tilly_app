package com.seeho.tilly.core.domain.repository

import com.seeho.tilly.core.model.CoinTransaction
import com.seeho.tilly.core.model.RewardResult
import com.seeho.tilly.core.model.UserCoin
import kotlinx.coroutines.flow.Flow

/**
 * 코인 Repository 인터페이스
 * 코인 잔액 관리 + 일일 보상 + 스트릭 관리 + 거래 내역
 */
interface CoinRepository {

    /** 유저 코인 정보 실시간 조회 */
    fun getUserCoin(): Flow<UserCoin>

    /** 출석 보상 지급 (보상 목록 반환, 이미 수령한 경우 null) */
    suspend fun claimAttendance(): RewardResult?

    /** TIL 작성 보상 지급 (보상 목록 반환, 이미 수령한 경우 null) */
    suspend fun claimTilReward(): RewardResult?

    /** 코인 추가 (범용) */
    suspend fun addCoins(amount: Int)

    /** 코인 차감 (잔액 부족 시 false) */
    suspend fun deductCoins(amount: Int): Boolean

    /** 일일 플래그 리셋 (날짜 변경 감지 시 호출) */
    suspend fun resetDailyFlagsIfNeeded()

    /** 코인 거래 내역 조회 (최신순) */
    fun getCoinTransactions(): Flow<List<CoinTransaction>>

    /** AI 분석 가능 여부 (무료 횟수 남았거나 코인 충분) */
    suspend fun canAnalyze(): Boolean

    /** AI 분석 1회 소비 (무료면 카운트 증가, 유료면 코인 차감). 불가 시 false */
    suspend fun consumeAnalysis(): Boolean

    /** 남은 무료 분석 횟수 (0이면 유료) */
    suspend fun getRemainingFreeAnalysis(): Int

    /** 현재 코인 잔액 */
    suspend fun getBalance(): Int

    /** 월간 회고 생성 가능 여부 (이번 달 무료 가능하거나 코인 충분) */
    suspend fun canGenerateRetrospective(month: Int, year: Int): Boolean

    /** 회고 생성권 소비 (무료면 월 기록, 유료면 코인 차감). 불가 시 false */
    suspend fun consumeRetrospective(month: Int, year: Int): Boolean
}
