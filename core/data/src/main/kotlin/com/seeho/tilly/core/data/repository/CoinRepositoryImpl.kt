package com.seeho.tilly.core.data.repository

import com.seeho.tilly.core.data.mapper.toModel
import com.seeho.tilly.core.database.dao.CoinDao
import com.seeho.tilly.core.database.entity.CoinEntity
import com.seeho.tilly.core.domain.repository.CoinRepository
import com.seeho.tilly.core.model.UserCoin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject

/**
 * CoinRepository 구현체
 * 코인 잔액 관리 + 일일 보상 플래그 + 스트릭 카운터
 */
class CoinRepositoryImpl @Inject constructor(
    private val coinDao: CoinDao,
) : CoinRepository {

    companion object {
        // 보상 금액 상수
        private const val ATTENDANCE_REWARD = 5     // 출석 보상
        private const val TIL_REWARD = 20           // TIL 작성 보상
        private const val STREAK_3_BONUS = 10       // 3일 연속 보너스
        private const val STREAK_7_BONUS = 30       // 7일 연속 보너스
        private const val STREAK_30_BONUS = 100     // 30일 연속 보너스
    }

    // 코인 조작 동시성 보호용 Mutex
    private val coinMutex = Mutex()

    override fun getUserCoin(): Flow<UserCoin> {
        return coinDao.getUserCoin().map { entity ->
            entity?.toModel() ?: UserCoin()  // 없으면 기본값 반환
        }
    }

    override suspend fun claimAttendance(): Boolean = coinMutex.withLock {
        ensureCoinExists()
        resetDailyFlagsIfNeeded()

        val current = coinDao.getUserCoin().firstOrNull() ?: return@withLock false
        // 이미 출석 보상을 받았으면 false 반환
        if (current.dailyAttendanceClaimed) return@withLock false

        coinDao.addCoins(ATTENDANCE_REWARD)
        coinDao.setDailyAttendanceClaimed(true)
        true
    }

    override suspend fun claimTilReward(): Boolean = coinMutex.withLock {
        ensureCoinExists()
        resetDailyFlagsIfNeeded()

        val current = coinDao.getUserCoin().firstOrNull() ?: return@withLock false
        // 오늘 이미 TIL 보상을 받았으면 false 반환
        if (current.dailyTilClaimed) return@withLock false

        // TIL 기본 보상 지급
        coinDao.addCoins(TIL_REWARD)
        coinDao.setDailyTilClaimed(true)

        // 스트릭 업데이트 + 보너스 지급
        val newStreak = current.streakCount + 1
        coinDao.updateStreakCount(newStreak)
        checkAndApplyStreakBonus(newStreak)

        true
    }

    override suspend fun addCoins(amount: Int) {
        require(amount > 0) { "추가할 코인은 양수여야 합니다: $amount" }
        coinMutex.withLock {
            ensureCoinExists()
            coinDao.addCoins(amount)
        }
    }

    override suspend fun deductCoins(amount: Int): Boolean {
        require(amount > 0) { "차감할 코인은 양수여야 합니다: $amount" }
        return coinMutex.withLock {
            ensureCoinExists()
            val current = coinDao.getUserCoin().firstOrNull() ?: return@withLock false
            // 잔액 부족 체크
            if (current.balance < amount) return@withLock false
            coinDao.deductCoins(amount)
            true
        }
    }

    override suspend fun resetDailyFlagsIfNeeded() {
        val today = LocalDate.now()
        val todayStr = today.format(DateTimeFormatter.ISO_LOCAL_DATE)
        val current = coinDao.getUserCoin().firstOrNull()

        // 날짜가 변경되었거나 데이터가 없으면 리셋
        if (current == null || current.lastClaimedDate != todayStr) {
            if (current != null) {
                // 마지막 접속일과 오늘 사이의 간격 계산
                val lastDate = try {
                    LocalDate.parse(current.lastClaimedDate, DateTimeFormatter.ISO_LOCAL_DATE)
                } catch (_: Exception) {
                    null
                }

                val daysBetween = lastDate?.let {
                    ChronoUnit.DAYS.between(it, today)
                } ?: Long.MAX_VALUE // 파싱 실패 시 스트릭 초기화

                when {
                    // 2일 이상 접속 안 한 경우 → 스트릭 무조건 초기화
                    daysBetween > 1 -> coinDao.updateStreakCount(0)
                    // 어제 접속했지만 TIL을 안 쓴 경우 → 스트릭 초기화
                    daysBetween == 1L && !current.dailyTilClaimed -> coinDao.updateStreakCount(0)
                    // daysBetween == 1 && dailyTilClaimed → 스트릭 유지
                }
            }
            coinDao.resetDailyFlags(todayStr)
        }
    }

    /** 코인 데이터가 없으면 초기 행 생성 */
    private suspend fun ensureCoinExists() {
        val existing = coinDao.getUserCoin().firstOrNull()
        if (existing == null) {
            coinDao.upsertCoin(CoinEntity())
        }
    }

    /** 스트릭 보너스 체크 및 지급 */
    private suspend fun checkAndApplyStreakBonus(streak: Int) {
        when (streak) {
            3 -> coinDao.addCoins(STREAK_3_BONUS)
            7 -> coinDao.addCoins(STREAK_7_BONUS)
            30 -> coinDao.addCoins(STREAK_30_BONUS)
        }
    }
}
