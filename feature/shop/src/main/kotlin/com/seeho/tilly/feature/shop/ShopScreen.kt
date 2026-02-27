package com.seeho.tilly.feature.shop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.seeho.tilly.core.designsystem.component.TillySnackbarHost
import com.seeho.tilly.core.model.ItemCategory
import com.seeho.tilly.core.model.ShopItem
import com.seeho.tilly.feature.shop.components.CategoryTabs
import com.seeho.tilly.feature.shop.components.CoinBadge
import com.seeho.tilly.feature.shop.components.DeskPreviewWithTillyRoom
import com.seeho.tilly.feature.shop.components.EquipmentItemGrid
import com.seeho.tilly.feature.shop.components.ThemeItemGrid

@Composable
fun ShopScreen(
    viewModel: ShopViewModel = hiltViewModel(),
) {
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val userCoin by viewModel.userCoin.collectAsStateWithLifecycle()
    val purchasedItemIds by viewModel.purchasedItemIds.collectAsStateWithLifecycle()
    val equippedItems by viewModel.equippedItems.collectAsStateWithLifecycle()
    val filteredItems by viewModel.filteredItems.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // 구매 결과 → 스낵바 표시 (SharedFlow 수집)
    LaunchedEffect(Unit) {
        viewModel.purchaseEvent.collect { result ->
            val message = when (result) {
                is PurchaseResult.Success -> "'${result.itemName}' 구매 완료! 🎉"
                is PurchaseResult.InsufficientFunds -> "코인이 부족해요 😢"
            }
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        snackbarHost = { TillySnackbarHost(hostState = snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        ShopContent(
            coinBalance = userCoin.balance,
            selectedCategory = selectedCategory,
            items = filteredItems,
            allItems = viewModel.allItems,
            purchasedItemIds = purchasedItemIds,
            equippedItems = equippedItems,
            onCategorySelect = viewModel::selectCategory,
            onPurchaseItem = viewModel::purchaseItem,
            onEquipItem = viewModel::equipItem,
            modifier = Modifier.padding(padding),
        )
    }
}

@Composable
private fun ShopContent(
    coinBalance: Int,
    selectedCategory: ItemCategory,
    items: List<ShopItem>,
    allItems: List<ShopItem>,
    purchasedItemIds: Set<String>,
    equippedItems: Map<ItemCategory, String>,
    onCategorySelect: (ItemCategory) -> Unit,
    onPurchaseItem: (ShopItem) -> Unit,
    onEquipItem: (ShopItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // 데스크 프리뷰 + 코인 뱃지
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
        ) {
            DeskPreviewWithTillyRoom(
                equippedItems = equippedItems,
                allItems = allItems,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 10f),
            )

            // 코인 잔액 뱃지
            CoinBadge(
                balance = coinBalance,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 카테고리 탭
        CategoryTabs(
            selectedCategory = selectedCategory,
            onCategorySelect = onCategorySelect,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 아이템 그리드
        if (selectedCategory == ItemCategory.THEME) {
            ThemeItemGrid(
                items = items,
                purchasedItemIds = purchasedItemIds,
                equippedThemeId = equippedItems[ItemCategory.THEME],
                onPurchaseItem = onPurchaseItem,
                onEquipItem = onEquipItem,
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            EquipmentItemGrid(
                items = items,
                purchasedItemIds = purchasedItemIds,
                equippedItemId = equippedItems[selectedCategory],
                onPurchaseItem = onPurchaseItem,
                onEquipItem = onEquipItem,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
