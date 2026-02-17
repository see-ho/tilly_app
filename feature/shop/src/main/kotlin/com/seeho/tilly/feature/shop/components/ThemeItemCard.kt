package com.seeho.tilly.feature.shop.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seeho.tilly.core.designsystem.component.TillyCard
import com.seeho.tilly.core.designsystem.theme.*
import com.seeho.tilly.core.model.ShopItem

/**
 * 테마 아이템 2열 그리드
 */
@Composable
fun ThemeItemGrid(
    items: List<ShopItem>,
    purchasedItemIds: Set<String>,
    equippedThemeId: String?,
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
            val isEquipped = item.id == equippedThemeId

            ThemeItemCard(
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
 * 테마 아이템 카드 — TillyCard 활용
 * PRIMARY/SEC/ACC 색상 프리뷰 + 코드 스니펫
 */
@Composable
fun ThemeItemCard(
    item: ShopItem,
    isPurchased: Boolean,
    isEquipped: Boolean,
    onPurchaseItem: (ShopItem) -> Unit,
    onEquipItem: (ShopItem) -> Unit,
) {
    val themeColors = getThemePreviewColors(item.id)

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
        Box {
            Column {
                // PRIMARY 색상 블록
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(themeColors.primary)
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("PRIMARY", color = themeColors.onPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(6.dp))

                // SEC + ACC
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(themeColors.secondary)
                            .padding(vertical = 4.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("SEC", color = themeColors.onSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(themeColors.accent)
                            .padding(vertical = 4.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("ACC", color = themeColors.onAccent, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // 코드 스니펫 미리보기
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(themeColors.codeBackground)
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                ) {
                    Text(
                        text = "const theme = 'code'",
                        color = themeColors.codeForeground,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 테마 이름 + 설명
                Text(
                    text = item.name,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                )
                Text(
                    text = item.description,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp,
                    lineHeight = 14.sp,
                    minLines = 2,
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 구매/장착 버튼
                ShopItemActionButton(
                    isPurchased = isPurchased,
                    isEquipped = isEquipped,
                    price = item.price,
                    onPurchase = { onPurchaseItem(item) },
                    onEquip = { onEquipItem(item) },
                )
            }

            // 보유 뱃지 — 우상단 아웃라인 칩 + 체크 아이콘
            if (isPurchased) {
                OwnedBadge(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp),
                )
            }
        }
    }
}

// ============================================
// 테마 프리뷰 색상 데이터
// ============================================

internal data class ThemePreviewColors(
    val primary: Color,
    val onPrimary: Color,
    val secondary: Color,
    val onSecondary: Color,
    val accent: Color,
    val onAccent: Color,
    val codeBackground: Color,
    val codeForeground: Color,
)

/** 테마 ID → 프리뷰 색상 (Color.kt의 테마 색상 상수 참조) */
internal fun getThemePreviewColors(themeId: String): ThemePreviewColors = when (themeId) {
    "theme_tilly_green" -> ThemePreviewColors(
        primary = NeoTerminalGreen, onPrimary = Color.Black,
        secondary = NeoTerminalGreenDim, onSecondary = Color.Black,
        accent = NeoTerminalGreenBright, onAccent = Color.Black,
        codeBackground = NeoDarkSurfaceVariant, codeForeground = NeoTerminalGreen,
    )
    "theme_dracula" -> ThemePreviewColors(
        primary = DraculaPurple, onPrimary = Color.White,
        secondary = DraculaPink, onSecondary = Color.Black,
        accent = DraculaGreen, onAccent = Color.Black,
        codeBackground = DraculaBackground, codeForeground = DraculaForeground,
    )
    "theme_monokai" -> ThemePreviewColors(
        primary = MonokaiPink, onPrimary = Color.White,
        secondary = MonokaiGreen, onSecondary = Color.Black,
        accent = MonokaiCyan, onAccent = Color.Black,
        codeBackground = MonokaiBackground, codeForeground = MonokaiForeground,
    )
    "theme_one_dark" -> ThemePreviewColors(
        primary = OneDarkBlue, onPrimary = Color.Black,
        secondary = OneDarkGreen, onSecondary = Color.Black,
        accent = OneDarkPurple, onAccent = Color.White,
        codeBackground = OneDarkBackground, codeForeground = OneDarkForeground,
    )
    "theme_nord" -> ThemePreviewColors(
        primary = NordFrost1, onPrimary = Color.Black,
        secondary = NordFrost2, onSecondary = Color.White,
        accent = NordAurora0, onAccent = Color.White,
        codeBackground = NordPolarNight0, codeForeground = NordSnowStorm2,
    )
    "theme_gruvbox" -> ThemePreviewColors(
        primary = GruvboxAqua, onPrimary = Color.Black,
        secondary = GruvboxYellow, onSecondary = Color.Black,
        accent = GruvboxRed, onAccent = Color.White,
        codeBackground = GruvboxBackground, codeForeground = GruvboxForeground,
    )
    else -> ThemePreviewColors(
        primary = NeoTerminalGreen, onPrimary = Color.Black,
        secondary = NeoDarkSurface, onSecondary = Color.White,
        accent = NeoGray400, onAccent = Color.White,
        codeBackground = NeoDarkBase, codeForeground = NeoGray300,
    )
}
