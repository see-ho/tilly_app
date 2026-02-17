package com.seeho.tilly.feature.shop.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seeho.tilly.core.designsystem.R

/**
 * 상점 아이템 공통 액션 버튼
 * 장착됨 / 장착하기 / 구매 상태에 따라 자동으로 표시
 */
@Composable
fun ShopItemActionButton(
    isPurchased: Boolean,
    isEquipped: Boolean,
    price: Int,
    onPurchase: () -> Unit,
    onEquip: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when {
        // 이미 장착된 상태 — 비활성 표시
        isEquipped -> {
            Button(
                onClick = { },
                enabled = false,
                modifier = modifier.fillMaxWidth().height(32.dp),
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = MaterialTheme.colorScheme.primary,
                    disabledContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
            ) {
                Text("장착됨", fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
            }
        }
        // 구매 완료 → 장착 가능 상태
        isPurchased -> {
            Button(
                onClick = onEquip,
                modifier = modifier.fillMaxWidth().height(32.dp),
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.outline,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                ),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
            ) {
                Text("장착하기", fontSize = 11.sp, fontFamily = FontFamily.Monospace)
            }
        }
        // 미구매 → 구매 버튼
        else -> {
            Button(
                onClick = onPurchase,
                modifier = modifier.fillMaxWidth().height(32.dp),
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary,
                ),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_coin),
                        contentDescription = "코인",
                        modifier = Modifier.size(12.dp),
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "$price",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                    )
                }
            }
        }
    }
}
