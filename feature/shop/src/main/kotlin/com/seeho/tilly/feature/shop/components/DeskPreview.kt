package com.seeho.tilly.feature.shop.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.seeho.tilly.core.designsystem.R
import com.seeho.tilly.core.designsystem.component.TillyRoom
import com.seeho.tilly.core.model.ItemCategory
import com.seeho.tilly.core.model.ShopItem

/**
 * 데스크 프리뷰 — TillyRoom 컴포넌트 활용
 * 장착 중인 아이템 ID → imageResName → drawable ID 변환하여 TillyRoom에 전달
 */
@Composable
fun DeskPreviewWithTillyRoom(
    equippedItems: Map<ItemCategory, String>,
    allItems: List<ShopItem>,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    // 장착 아이템 ID → imageResName 조회
    fun findImageResName(category: ItemCategory): String? {
        val itemId = equippedItems[category] ?: return null
        return allItems.find { it.id == itemId }?.imageResName
    }

    // 공통 유틸로 imageResName → drawable ID 변환

    // 각 카테고리별 장착 아이템의 drawable ID
    val chairDrawableId = resolveDrawableId(context, findImageResName(ItemCategory.CHAIR))
    val deskDrawableId = resolveDrawableId(context, findImageResName(ItemCategory.DESK))
    val comDrawableId = resolveDrawableId(context, findImageResName(ItemCategory.EQUIPMENT))
    val keyboardDrawableId = resolveDrawableId(context, findImageResName(ItemCategory.KEYBOARD))
    val decoDrawableId = resolveDrawableId(context, findImageResName(ItemCategory.DECORATION))

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        // TillyRoom: 장착 아이템에 따라 동적으로 변경
        TillyRoom(
            modifier = Modifier.fillMaxSize(),
            chairRes = if (chairDrawableId != 0) chairDrawableId else R.drawable.bg_chair_basic,
            deskRes = if (deskDrawableId != 0) deskDrawableId else R.drawable.bg_desk_basic,
            comRes = if (comDrawableId != 0) comDrawableId else R.drawable.gif_obj_basic_com,
            keyboardRes = if (keyboardDrawableId != 0) keyboardDrawableId else R.drawable.obj_keyboard_basic,
            deskItemRes = if (decoDrawableId != 0) decoDrawableId else null,
        )
    }
}
