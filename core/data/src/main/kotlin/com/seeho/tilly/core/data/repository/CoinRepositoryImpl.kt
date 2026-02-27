package com.seeho.tilly.core.data.repository

import com.seeho.tilly.core.data.mapper.toModel
import com.seeho.tilly.core.data.reward.RewardPolicy
import com.seeho.tilly.core.database.TillyDatabase
import com.seeho.tilly.core.database.dao.CoinDao
import com.seeho.tilly.core.database.dao.CoinTransactionDao
import com.seeho.tilly.core.database.entity.CoinEntity
import com.seeho.tilly.core.database.entity.CoinTransactionEntity
import com.seeho.tilly.core.database.withDatabaseTransaction
import com.seeho.tilly.core.domain.repository.CoinRepository
import com.seeho.tilly.core.model.CoinTransaction
import com.seeho.tilly.core.model.CoinTransactionType
import com.seeho.tilly.core.model.RewardResult
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
 * 코인 잔액 관리 + 일일 보상 플래그 + 스트릭 카운터 + 거래 내역 기록
 * 코인 변경과 거래 내역 기록은 withTransaction으로 원자적으로 묶어 데이터 무결성 보장
 */
class CoinRepositoryImpl @Inject constructor(
    private val coinDao: CoinDao,
    private val coinTransactionDao: CoinTransactionDao,
    private val database: TillyDatabase,
) : CoinRepository {

    // 코인 조작 동시성 보호용 Mutex
    private val coinMutex = Mutex()

    override fun getUserCoin(): Flow<UserCoin> {
        return coinDao.getUserCoin().map { entity ->
            entity?.toModel() ?: UserCoin()  // 없으면 기본값 반환
        }
    }

    override fun getCoinTransactions(): Flow<List<CoinTransaction>> {
        return coinTransactionDao.getAllTransactions().map { entities ->
            entities.map { it.toModel() }
        }
    }

    override suspend fun claimAttendance(): RewardResult? = coinMutex.withLock {
        ensureCoinExists()
        resetDailyFlagsIfNeeded()

        val current = coinDao.getUserCoin().firstOrNull() ?: return@withLock null
        // 이미 출석 보상을 받았으면 null 반환
        if (current.dailyAttendanceClaimed) return@withLock null

        val reward = RewardPolicy.attendanceReward()

        // 코인 변경 + 거래 내역 처리
        database.withDatabaseTransaction {
            coinDao.addCoins(reward.amount)
            coinDao.setDailyAttendanceClaimed(true)
            recordTransaction(
                amount = reward.amount,
                type = CoinTransactionType.ATTENDANCE,
                description = reward.description,
            )
        }
        RewardResult(rewards = listOf(reward))
    }

    override suspend fun claimTilReward(): RewardResult? = coinMutex.withLock {
        ensureCoinExists()
        resetDailyFlagsIfNeeded()

        val current = coinDao.getUserCoin().firstOrNull() ?: return@withLock null
        // 오늘 이미 TIL 보상을 받았으면 null 반환
        if (current.dailyTilClaimed) return@withLock null

        // 보상 항목 수집
        val rewards = mutableListOf(RewardPolicy.tilReward())
        val tilReward = rewards.first()

        database.withDatabaseTransaction {
            coinDao.addCoins(tilReward.amount)
            coinDao.setDailyTilClaimed(true)
            recordTransaction(
                amount = tilReward.amount,
                type = CoinTransactionType.TIL_REWARD,
                description = tilReward.description,
            )

            // 스트릭 업데이트 + 보너스 지급
            val newStreak = current.streakCount + 1
            coinDao.updateStreakCount(newStreak)

            // 스트릭 보너스가 있으면 rewards에 추가
            RewardPolicy.getStreakBonus(newStreak)?.let { bonus ->
                coinDao.addCoins(bonus.amount)
                rewards.add(bonus)
                recordTransaction(
                    amount = bonus.amount,
                    type = CoinTransactionType.STREAK_BONUS,
                    description = bonus.description,
                )
            }
        }

        RewardResult(rewards = rewards)
    }

    override suspend fun addCoins(amount: Int) {
        require(amount > 0) { "추가할 코인은 양수여야 합니다: $amount" }
        coinMutex.withLock {
            ensureCoinExists()
            database.withDatabaseTransaction {
                coinDao.addCoins(amount)
                recordTransaction(
                    amount = amount,
                    type = CoinTransactionType.AD_REWARD,
                    description = "광고 시청 보상",
                )
            }
        }
    }

    override suspend fun deductCoins(amount: Int): Boolean {
        require(amount > 0) { "차감할 코인은 양수여야 합니다: $amount" }
        return coinMutex.withLock {
            ensureCoinExists()
            val current = coinDao.getUserCoin().firstOrNull() ?: return@withLock false
            // 잔액 부족 체크
            if (current.balance < amount) return@withLock false
            database.withDatabaseTransaction {
                coinDao.deductCoins(amount)
                recordTransaction(
                    amount = -amount,
                    type = CoinTransactionType.PURCHASE,
                    description = "아이템 구매",
                )
            }
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



    /**
     * 현재 잔액을 조회하여 balanceAfter를 채움
     */
    private suspend fun recordTransaction(
        amount: Int,
        type: CoinTransactionType,
        description: String,
    ) {
        val currentBalance = coinDao.getBalanceSync() ?: 0
        coinTransactionDao.insertTransaction(
            CoinTransactionEntity(
                amount = amount,
                type = type.name,
                description = description,
                balanceAfter = currentBalance,
                createdAt = System.currentTimeMillis(),
            ),
        )
    }
}
