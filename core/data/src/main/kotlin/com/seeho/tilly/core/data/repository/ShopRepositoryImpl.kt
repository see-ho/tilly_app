package com.seeho.tilly.core.data.repository

import com.seeho.tilly.core.data.shop.ShopItemData
import com.seeho.tilly.core.database.dao.ShopDao
import com.seeho.tilly.core.domain.repository.CoinRepository
import com.seeho.tilly.core.database.entity.EquippedItemEntity
import com.seeho.tilly.core.database.entity.PurchasedItemEntity
import com.seeho.tilly.core.domain.repository.ShopRepository
import com.seeho.tilly.core.model.ItemCategory
import com.seeho.tilly.core.model.ShopItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

/**
 * ShopRepository 구현체
 * 아이템 목록 + 구매(코인 차감) + 장착 관리
 */
class ShopRepositoryImpl @Inject constructor(
    private val shopDao: ShopDao,
    private val coinRepository: CoinRepository,
) : ShopRepository {

    // 기본 장착 초기화 중복 방지 플래그
    private var defaultEquipmentInitialized = false

    override fun getAllShopItems(): List<ShopItem> = ShopItemData.allItems

    override fun getPurchasedItemIds(): Flow<Set<String>> {
        return shopDao.getAllPurchasedItems().map { entities ->
            entities.map { it.itemId }.toSet()
        }
    }

    override fun getEquippedItems(): Flow<Map<ItemCategory, String>> {
        return shopDao.getEquippedItems()
            // 초기 실행 시 equipped_items가 비어있으면 기본 아이템 자동 장착
            .onStart {
                if (!defaultEquipmentInitialized) {
                    defaultEquipmentInitialized = true
                    val snapshot = shopDao.getEquippedItemsSnapshot()
                    if (snapshot.isEmpty()) {
                        initializeDefaultEquipment()
                    }
                }
            }
            .map { entities ->
                // 잘못된 카테고리가 있는 entity만 건너뛰고 나머지는 유지
                entities.mapNotNull { entity ->
                    val category = try {
                        ItemCategory.valueOf(entity.category)
                    } catch (_: IllegalArgumentException) {
                        return@mapNotNull null
                    }
                    category to entity.itemId
                }.toMap()
            }
    }

    /**
     * 기본 아이템을 각 카테고리별로 장착 등록
     * 앱 최초 실행 시 한 번만 호출됨
     */
    private suspend fun initializeDefaultEquipment() {
        ShopItemData.allItems
            .filter { it.isDefault }
            .forEach { item ->
                shopDao.equipItem(
                    EquippedItemEntity(
                        category = item.category.name,
                        itemId = item.id,
                    )
                )
            }
    }

    override suspend fun purchaseItem(itemId: String, price: Int): Boolean {
        // 이미 구매한 아이템인지 확인
        if (shopDao.isItemPurchased(itemId)) return false

        // CoinRepository를 통해 코인 차감 (잔액 부족 시 false 반환)
        if (!coinRepository.deductCoins(price)) return false

        // 구매 기록 저장 — 실패 시 코인 복구(롤백)
        try {
            shopDao.purchaseItem(
                PurchasedItemEntity(
                    itemId = itemId,
                    purchasedAt = System.currentTimeMillis(),
                )
            )
        } catch (e: Exception) {
            // 구매 기록 실패 → 차감된 코인 복구
            coinRepository.addCoins(price)
            return false
        }
        return true
    }

    override suspend fun equipItem(category: ItemCategory, itemId: String) {
        shopDao.equipItem(
            EquippedItemEntity(
                category = category.name,
                itemId = itemId,
            )
        )
    }
}
