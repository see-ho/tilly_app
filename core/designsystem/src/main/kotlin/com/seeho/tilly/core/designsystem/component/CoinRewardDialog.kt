package com.seeho.tilly.core.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seeho.tilly.core.designsystem.R
import kotlinx.coroutines.delay

/**
 * 코인 보상 알림 다이얼로그
 * gif_coin.gif 애니메이션과 함께 획득한 코인 수를 표시
 * 일정 시간 후 자동 사라지거나 터치 시 닫힘
 */
@Composable
fun CoinRewardDialog(
    visible: Boolean,
    amount: Int,
    reason: String,
    onDismiss: () -> Unit,
    autoDismissMs: Long = 2000L,
) {
    // 자동 닫힘 타이머
    if (visible) {
        LaunchedEffect(Unit) {
            delay(autoDismissMs)
            onDismiss()
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + scaleIn(initialScale = 0.8f),
        exit = fadeOut() + scaleOut(targetScale = 0.8f),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                ) { onDismiss() },
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                // 코인 GIF 애니메이션
                GifAsset(
                    gifRes = R.drawable.gif_coin,
                    contentDescription = "코인 보상",
                    modifier = Modifier.size(120.dp),
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 획득 코인 수
                Text(
                    text = "+$amount",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                )

                Spacer(modifier = Modifier.height(4.dp))

                // 보상 사유
                Text(
                    text = reason,
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace,
                )
            }
        }
    }
}
