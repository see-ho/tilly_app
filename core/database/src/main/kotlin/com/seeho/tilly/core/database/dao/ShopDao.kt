package com.seeho.tilly.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.seeho.tilly.core.database.entity.EquippedItemEntity
import com.seeho.tilly.core.database.entity.PurchasedItemEntity
import kotlinx.coroutines.flow.Flow

/**
 * 상점 아이템 관련 DAO
 * 구매한 아이템 목록 및 장착 아이템 관리
 */
@Dao
interface ShopDao {

    /** 구매한 아이템 전체 조회 */
    @Query("SELECT * FROM purchased_items")
    fun getAllPurchasedItems(): Flow<List<PurchasedItemEntity>>

    /** 아이템 구매 기록 추가 */
    @Insert
    suspend fun purchaseItem(item: PurchasedItemEntity)

    /** 특정 아이템 구매 여부 확인 */
    @Query("SELECT COUNT(*) > 0 FROM purchased_items WHERE itemId = :itemId")
    suspend fun isItemPurchased(itemId: String): Boolean

    /** 장착 중인 아이템 전체 조회 */
    @Query("SELECT * FROM equipped_items")
    fun getEquippedItems(): Flow<List<EquippedItemEntity>>

    /** 아이템 장착 (카테고리별 1개, Upsert로 교체) */
    @Upsert
    suspend fun equipItem(item: EquippedItemEntity)
}
