package com.seeho.tilly.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.seeho.tilly.core.database.entity.CoinTransactionEntity
import kotlinx.coroutines.flow.Flow

/**
 * 코인 거래 내역 DAO
 */
@Dao
interface CoinTransactionDao {

    /** 거래 내역 삽입 */
    @Insert
    suspend fun insertTransaction(transaction: CoinTransactionEntity)

    /** 전체 거래 내역 조회 (최신순) */
    @Query("SELECT * FROM coin_transaction ORDER BY createdAt DESC")
    fun getAllTransactions(): Flow<List<CoinTransactionEntity>>

}
