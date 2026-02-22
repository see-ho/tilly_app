package com.seeho.tilly.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.seeho.tilly.core.database.entity.CoinEntity
import kotlinx.coroutines.flow.Flow

/**
 * 코인 관련 DAO
 */
@Dao
interface CoinDao {

    /** 유저 코인 정보 조회 (Flow로 실시간 관찰) */
    @Query("SELECT * FROM user_coin WHERE id = 1")
    fun getUserCoin(): Flow<CoinEntity?>

    /** 코인 정보 생성 또는 업데이트 (Upsert) */
    @Upsert
    suspend fun upsertCoin(coin: CoinEntity)

    /** 코인 추가 (보상 지급) */
    @Query("UPDATE user_coin SET balance = balance + :amount WHERE id = 1")
    suspend fun addCoins(amount: Int)

    /** 코인 차감 (아이템 구매) */
    @Query("UPDATE user_coin SET balance = balance - :amount WHERE id = 1")
    suspend fun deductCoins(amount: Int)

    /** TIL 작성 보상 수령 플래그 설정 */
    @Query("UPDATE user_coin SET dailyTilClaimed = :claimed WHERE id = 1")
    suspend fun setDailyTilClaimed(claimed: Boolean)

    /** 출석 보상 수령 플래그 설정 */
    @Query("UPDATE user_coin SET dailyAttendanceClaimed = :claimed WHERE id = 1")
    suspend fun setDailyAttendanceClaimed(claimed: Boolean)

    /** 광고 시청 횟수 증가 */
    @Query("UPDATE user_coin SET dailyAdWatchCount = dailyAdWatchCount + 1 WHERE id = 1")
    suspend fun incrementAdWatchCount()

    /** 스트릭 카운트 업데이트 */
    @Query("UPDATE user_coin SET streakCount = :count WHERE id = 1")
    suspend fun updateStreakCount(count: Int)

    /** 일일 플래그 리셋 (날짜 변경 시) */
    @Query("UPDATE user_coin SET dailyTilClaimed = 0, dailyAttendanceClaimed = 0, dailyAdWatchCount = 0, lastClaimedDate = :date WHERE id = 1")
    suspend fun resetDailyFlags(date: String)

    /** 트랜잭션 내에서 현재 잔액을 즉시 조회 */
    @Query("SELECT balance FROM user_coin WHERE id = 1")
    suspend fun getBalanceSync(): Int?
}
