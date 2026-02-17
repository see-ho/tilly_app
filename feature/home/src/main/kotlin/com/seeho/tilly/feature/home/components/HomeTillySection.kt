package com.seeho.tilly.feature.home.components

import android.content.res.Configuration

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seeho.tilly.core.designsystem.component.TillyRoom
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.seeho.tilly.core.designsystem.theme.TillyTheme
import com.seeho.tilly.core.model.ItemCategory
import com.seeho.tilly.core.model.ShopItem

/**
 * 홈 화면 Tilly 섹션
 * 장착 아이템에 따라 TillyRoom을 동적으로 렌더링
 */
@Composable
fun HomeTillySection(
    equippedItems: Map<ItemCategory, String> = emptyMap(),
    allShopItems: List<ShopItem> = emptyList(),
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    // 장착 아이템 ID → imageResName 조회
    fun findImageResName(category: ItemCategory): String? {
        val itemId = equippedItems[category] ?: return null
        return allShopItems.find { it.id == itemId }?.imageResName
    }

    // imageResName → drawable ID 변환
    fun resolveDrawableId(resName: String?): Int {
        if (resName == null) return 0
        return context.resources.getIdentifier(resName, "drawable", context.packageName)
    }

    // 각 카테고리별 장착 아이템의 drawable ID
    val chairDrawableId = resolveDrawableId(findImageResName(ItemCategory.CHAIR))
    val deskDrawableId = resolveDrawableId(findImageResName(ItemCategory.DESK))
    val comDrawableId = resolveDrawableId(findImageResName(ItemCategory.EQUIPMENT))
    val keyboardDrawableId = resolveDrawableId(findImageResName(ItemCategory.KEYBOARD))
    val decoDrawableId = resolveDrawableId(findImageResName(ItemCategory.DECORATION))

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp) // 상단 여백 보정
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)),
        contentAlignment = Alignment.Center
    ) {
        TillyRoom(
            modifier = Modifier.padding(16.dp),
            chairRes = if (chairDrawableId != 0) chairDrawableId else com.seeho.tilly.core.designsystem.R.drawable.bg_chair_basic,
            deskRes = if (deskDrawableId != 0) deskDrawableId else com.seeho.tilly.core.designsystem.R.drawable.bg_desk_basic,
            comRes = if (comDrawableId != 0) comDrawableId else com.seeho.tilly.core.designsystem.R.drawable.gif_obj_basic_com,
            keyboardRes = if (keyboardDrawableId != 0) keyboardDrawableId else com.seeho.tilly.core.designsystem.R.drawable.obj_keyboard_basic,
            deskItemRes = if (decoDrawableId != 0) decoDrawableId else null,
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeTillySectionPreview() {
    TillyTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            HomeTillySection()
        }
    }
}
