package com.seeho.tilly.feature.mypage.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seeho.tilly.core.designsystem.component.TillyCard
import com.seeho.tilly.core.designsystem.theme.TillyTheme

/**
 * 주간 TIL 작성 체크 카드
 */
@Composable
fun WeeklyCheckCard(
    weeklyCheck: List<Boolean>,
    modifier: Modifier = Modifier,
) {
    val dayLabels = listOf("월", "화", "수", "목", "금", "토", "일")
    val checkedCount = weeklyCheck.count { it }

    // 체크 수에 따른 격려 메시지
    val encourageMessage = when (checkedCount) {
        0 -> "✨ 오늘부터 시작해봐요!"
        1 -> "🌱 좋은 시작이에요! 조금씩 채워가봐요!"
        2 -> "🌿 잘 하고 있어요! 꾸준함이 힘이에요!"
        3 -> "💪 절반 가까이 왔어요! 대단해요!"
        4 -> "🔥 반 이상 달성! 이 기세를 유지해요!"
        5 -> "⭐ 거의 다 왔어요! 조금만 더 힘내요!"
        6 -> "🏆 완벽에 가까워요! 하루만 더!"
        7 -> "🎉 완벽한 한 주! 틸리가 감동받았어요!"
        else -> "🌱 조금씩 채워가봐요!"
    }

    TillyCard(modifier = modifier) {
        Column{
            // 제목
            Text(
                text = "Weekly Check",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )

            // 요일별 체크 원
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                dayLabels.forEachIndexed { index, label ->
                    val isChecked = weeklyCheck.getOrElse(index) { false }
                    DayCheckItem(
                        label = label,
                        isChecked = isChecked,
                    )
                }
            }

            // 격려 메시지
            Text(
                text = encourageMessage,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
            )
        }
    }
}

/**
 * 개별 요일 체크 아이템
 */
@Composable
private fun DayCheckItem(
    label: String,
    isChecked: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // 체크 원
        Box(
            modifier = Modifier
                .size(36.dp)
                .then(
                    if (isChecked) Modifier.shadow(
                        elevation = 10.dp,
                        shape = CircleShape,
                        ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                        spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                    ) else Modifier
                )
                .clip(CircleShape)
                .background(
                    if (isChecked) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.surfaceVariant,
                ),
            contentAlignment = Alignment.Center,
        ) {
            if (isChecked) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "$label 완료",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(20.dp),
                )
            }
        }

        // 요일 라벨
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (isChecked) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun WeeklyCheckCardPreview() {
    TillyTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            WeeklyCheckCard(
                weeklyCheck = listOf(true, true, false, false, false, false, false),
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}
