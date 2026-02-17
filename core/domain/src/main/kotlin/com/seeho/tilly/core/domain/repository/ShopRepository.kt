package com.seeho.tilly.core.domain.repository

import com.seeho.tilly.core.model.ItemCategory
import com.seeho.tilly.core.model.ShopItem
import kotlinx.coroutines.flow.Flow

/**
 * 상점 Repository 인터페이스
 * 아이템 목록 조회 + 구매 + 장착 관리
 */
interface ShopRepository {

    /** 전체 상점 아이템 목록 조회 */
    fun getAllShopItems(): List<ShopItem>

    /** 구매한 아이템 ID 목록 (실시간) */
    fun getPurchasedItemIds(): Flow<Set<String>>

    /** 장착 중인 아이템 (카테고리 → 아이템ID) */
    fun getEquippedItems(): Flow<Map<ItemCategory, String>>

    /** 아이템 구매 (코인 차감 + 기록 저장, 성공 시 true) */
    suspend fun purchaseItem(itemId: String, price: Int): Boolean

    /** 아이템 장착 */
    suspend fun equipItem(category: ItemCategory, itemId: String)
}
