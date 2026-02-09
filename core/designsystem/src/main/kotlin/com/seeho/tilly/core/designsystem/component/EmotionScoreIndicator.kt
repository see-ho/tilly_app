package com.seeho.tilly.core.designsystem.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.seeho.tilly.core.designsystem.theme.EmotionLevel1
import com.seeho.tilly.core.designsystem.theme.EmotionLevel2
import com.seeho.tilly.core.designsystem.theme.EmotionLevel3
import com.seeho.tilly.core.designsystem.theme.EmotionLevel4
import com.seeho.tilly.core.designsystem.theme.EmotionLevel5
import com.seeho.tilly.core.designsystem.theme.TillyTheme

/**
 * 감정 점수(1-5)를 5단계 블록으로 보여주는 인디케이터
 * 각 단계별로 고유한 감정 색상이 적용되며, 피드 항목 등에서 사용
 */
@Composable
fun EmotionScoreIndicator(
    score: Int,
    modifier: Modifier = Modifier,
    blockWidth: Dp = 16.dp,
    blockHeight: Dp = 10.dp,
) {
    val safeScore = score.coerceIn(0, 5)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        repeat(5) { index ->
            val isActive = index < safeScore
            val color = if (isActive) {
                listOf(
                    EmotionLevel1,
                    EmotionLevel2,
                    EmotionLevel3,
                    EmotionLevel4,
                    EmotionLevel5
                )[index]
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }

            Box(
                modifier = Modifier
                    .size(width = blockWidth, height = blockHeight)
                    .background(color)
            )
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun EmotionScoreIndicatorPreview() {
    TillyTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                EmotionScoreIndicator(score = 1)
                EmotionScoreIndicator(score = 3)
                EmotionScoreIndicator(score = 5)
            }
        }
    }
}
