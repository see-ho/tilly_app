package com.seeho.tilly.feature.shop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seeho.tilly.core.domain.EquipItemUseCase
import com.seeho.tilly.core.domain.GetShopItemsUseCase
import com.seeho.tilly.core.domain.GetUserCoinUseCase
import com.seeho.tilly.core.domain.PurchaseItemUseCase
import com.seeho.tilly.core.model.ItemCategory
import com.seeho.tilly.core.model.ShopItem
import com.seeho.tilly.core.model.UserCoin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 상점 화면 뷰모델
 * 아이템 목록, 카테고리 필터, 구매/장착 상태 관리
 */
@HiltViewModel
class ShopViewModel @Inject constructor(
    private val getShopItemsUseCase: GetShopItemsUseCase,
    private val getUserCoinUseCase: GetUserCoinUseCase,
    private val purchaseItemUseCase: PurchaseItemUseCase,
    private val equipItemUseCase: EquipItemUseCase,
) : ViewModel() {

    // 선택된 카테고리 탭
    private val _selectedCategory = MutableStateFlow(ItemCategory.EQUIPMENT)
    val selectedCategory: StateFlow<ItemCategory> = _selectedCategory.asStateFlow()

    // 유저 코인 정보
    val userCoin: StateFlow<UserCoin> = getUserCoinUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserCoin())

    // 구매한 아이템 ID 목록
    val purchasedItemIds: StateFlow<Set<String>> = getShopItemsUseCase.getPurchasedItemIds()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    // 장착 중인 아이템 (카테고리 → 아이템ID)
    val equippedItems: StateFlow<Map<ItemCategory, String>> = getShopItemsUseCase.getEquippedItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    // 전체 상점 아이템 목록
    val allItems: List<ShopItem> = getShopItemsUseCase.getAllItems()

    // 카테고리별 필터된 아이템
    val filteredItems: StateFlow<List<ShopItem>> = _selectedCategory
        .combine(MutableStateFlow(allItems)) { category, items ->
            items.filter { it.category == category }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // 구매 결과 이벤트
    private val _purchaseResult = MutableStateFlow<PurchaseResult?>(null)
    val purchaseResult: StateFlow<PurchaseResult?> = _purchaseResult.asStateFlow()

    /** 카테고리 탭 변경 */
    fun selectCategory(category: ItemCategory) {
        _selectedCategory.value = category
    }

    /** 아이템 구매 */
    fun purchaseItem(item: ShopItem) {
        viewModelScope.launch {
            val success = purchaseItemUseCase(item.id, item.price)
            _purchaseResult.value = if (success) {
                PurchaseResult.Success(item.name)
            } else {
                PurchaseResult.InsufficientFunds
            }
        }
    }

    /** 아이템 장착 — TillyRoom 프리뷰에 즉시 반영됨 (equippedItems Flow 갱신) */
    fun equipItem(item: ShopItem) {
        viewModelScope.launch {
            equipItemUseCase(item.category, item.id)
        }
    }

    /** 구매 결과 이벤트 소비 */
    fun consumePurchaseResult() {
        _purchaseResult.value = null
    }
}
