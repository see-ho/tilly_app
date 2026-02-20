package com.seeho.tilly.feature.mypage.components

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seeho.tilly.core.designsystem.component.TillyCard
import com.seeho.tilly.core.designsystem.theme.TillyTheme

/**
 * 활동 통계 카드 (스트릭 + TIL 수) 가로 대칭 배치
 */
@Composable
fun ActivityStatsRow(
    streakCount: Int,
    totalTilCount: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // 연속 스트릭 카드
        StatCard(
            iconResId = com.seeho.tilly.core.designsystem.R.drawable.ic_fire,
            iconDescription = "스트릭",
            value = "$streakCount",
            unit = "days",
            description = "연속 틸리와 공부중!",
            modifier = Modifier.weight(1f),
        )

        // 총 TIL 작성 수 카드
        StatCard(
            iconResId = com.seeho.tilly.core.designsystem.R.drawable.ic_save,
            iconDescription = "TIL",
            value = "$totalTilCount",
            unit = "",
            description = "개의 TIL 작성",
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun StatCard(
    @DrawableRes iconResId: Int,
    iconDescription: String,
    value: String,
    unit: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    TillyCard(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = iconResId),
                contentDescription = iconDescription,
                modifier = Modifier.size(32.dp),
            )

            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.padding(top = 4.dp),
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                if (unit.isNotEmpty()) {
                    Text(
                        text = " $unit",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 2.dp),
                    )
                }
            }

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ActivityStatsPreview() {
    TillyTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ActivityStatsRow(
                streakCount = 10,
                totalTilCount = 7,
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

