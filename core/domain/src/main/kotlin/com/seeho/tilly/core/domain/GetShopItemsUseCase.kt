package com.seeho.tilly.core.domain

import com.seeho.tilly.core.model.ShopItem
import com.seeho.tilly.core.domain.repository.ShopRepository
import com.seeho.tilly.core.model.ItemCategory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 상점 아이템 목록 조회 UseCase
 * 전체 아이템 + 구매/장착 상태를 함께 제공
 */
class GetShopItemsUseCase @Inject constructor(
    private val shopRepository: ShopRepository,
) {
    /** 전체 상점 아이템 목록 */
    fun getAllItems(): List<ShopItem> = shopRepository.getAllShopItems()

    /** 카테고리별 아이템 필터 */
    fun getItemsByCategory(category: ItemCategory): List<ShopItem> =
        shopRepository.getAllShopItems().filter { it.category == category }

    /** 구매한 아이템 ID 목록 */
    fun getPurchasedItemIds(): Flow<Set<String>> = shopRepository.getPurchasedItemIds()

    /** 장착 중인 아이템 (카테고리 → 아이템ID) */
    fun getEquippedItems(): Flow<Map<ItemCategory, String>> = shopRepository.getEquippedItems()
}
