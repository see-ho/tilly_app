package com.seeho.tilly.feature.shop.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seeho.tilly.core.designsystem.component.GifAsset
import com.seeho.tilly.core.designsystem.component.TillyCard
import com.seeho.tilly.core.model.ShopItem

/**
 * 장비 아이템 2열 그리드
 */
@Composable
fun EquipmentItemGrid(
    items: List<ShopItem>,
    purchasedItemIds: Set<String>,
    equippedItemId: String?,
    onPurchaseItem: (ShopItem) -> Unit,
    onEquipItem: (ShopItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(items, key = { it.id }) { item ->
            val isPurchased = item.isDefault || item.id in purchasedItemIds
            val isEquipped = item.id == equippedItemId

            EquipmentItemCard(
                item = item,
                isPurchased = isPurchased,
                isEquipped = isEquipped,
                onPurchaseItem = onPurchaseItem,
                onEquipItem = onEquipItem,
            )
        }
    }
}

/**
 * 장비 아이템 카드 — TillyCard 활용 + 실제 drawable 이미지
 */
@Composable
fun EquipmentItemCard(
    item: ShopItem,
    isPurchased: Boolean,
    isEquipped: Boolean,
    onPurchaseItem: (ShopItem) -> Unit,
    onEquipItem: (ShopItem) -> Unit,
) {
    val context = LocalContext.current
    val drawableId = resolveDrawableId(context, item.imageResName)

    TillyCard(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .then(
                if (isEquipped) Modifier.border(
                    width = 1.5.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(12.dp),
                ) else Modifier
            ),
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxWidth(),
        ) {
            // 아이템 이미지 + 보유 뱃지 오버레이
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center,
            ) {
                // 아이템 이미지 — GIF / 정적 이미지 분기
                if (drawableId != 0) {
                    if (item.isAnimated) {
                        GifAsset(
                            gifRes = drawableId,
                            contentDescription = item.name,
                            modifier = Modifier.fillMaxSize().padding(8.dp),
                        )
                    } else {
                        Image(
                            bitmap = ImageBitmap.imageResource(id = drawableId),
                            contentDescription = item.name,
                            modifier = Modifier.fillMaxSize().padding(8.dp),
                            filterQuality = FilterQuality.None,
                        )
                    }
                }

                // 보유 뱃지
                if (isPurchased) {
                    OwnedBadge(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 아이템 이름
            Text(
                text = item.name,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            // 아이템 설명
            Text(
                text = item.description,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.sp,
                lineHeight = 14.sp,
                minLines = 2,
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 하단 버튼 (구매 / 장착)
            ShopItemActionButton(
                isPurchased = isPurchased,
                isEquipped = isEquipped,
                price = item.price,
                onPurchase = { onPurchaseItem(item) },
                onEquip = { onEquipItem(item) },
            )
        }
    }
}
