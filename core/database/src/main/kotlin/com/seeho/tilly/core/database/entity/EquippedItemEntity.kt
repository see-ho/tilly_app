package com.seeho.tilly.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 현재 장착 중인 아이템 Room Entity
 * 카테고리별 1개씩만 장착 가능
 */
@Entity(tableName = "equipped_items")
data class EquippedItemEntity(
    @PrimaryKey val category: String,  // ItemCategory.name (예: "EQUIPMENT", "THEME")
    val itemId: String,                // ShopItem.id (예: "monitor_retro")
)
