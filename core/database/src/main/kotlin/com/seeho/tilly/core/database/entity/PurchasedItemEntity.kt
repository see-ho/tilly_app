package com.seeho.tilly.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 구매한 아이템 기록 Room Entity
 */
@Entity(tableName = "purchased_items")
data class PurchasedItemEntity(
    @PrimaryKey val itemId: String,  // ShopItem.id (예: "monitor_retro")
    val purchasedAt: Long,           // 구매 시각 (milliseconds)
)
