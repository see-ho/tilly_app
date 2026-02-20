package com.seeho.tilly.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 코인 거래 내역 Room Entity
 */
@Entity(tableName = "coin_transaction")
data class CoinTransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Int,
    val type: String,             // CoinTransactionType의 name
    val description: String,
    val balanceAfter: Int,        // 거래 후 잔액
    val createdAt: Long,          // System.currentTimeMillis()
)
