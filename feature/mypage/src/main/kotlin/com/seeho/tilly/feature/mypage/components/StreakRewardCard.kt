package com.seeho.tilly.feature.mypage.components

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seeho.tilly.core.designsystem.component.TillyCard
import com.seeho.tilly.core.designsystem.theme.TillyTheme

// 마일스톤 단계 정의
private val MILESTONES = listOf(7, 14, 30, 60, 100)

/**
 * 다음 마일스톤과 진행률 계산
 */
private fun getNextMilestone(streakCount: Int): Pair<Int, Float> {
    val nextMilestone = MILESTONES.firstOrNull { it > streakCount }
        ?: (MILESTONES.last() + 100)
    val prevMilestone = MILESTONES.lastOrNull { it <= streakCount } ?: 0
    val progress = if (nextMilestone == prevMilestone) 1f
    else (streakCount - prevMilestone).toFloat() / (nextMilestone - prevMilestone).toFloat()
    return nextMilestone to progress.coerceIn(0f, 1f)
}

/**
 * 다음 연속 학습 보상까지 진행 바 카드 (풀 너비)
 */
@Composable
fun StreakRewardCard(
    streakCount: Int,
    modifier: Modifier = Modifier,
) {
    val (nextMilestone, progress) = getNextMilestone(streakCount)
    val remaining = nextMilestone - streakCount

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 800),
        label = "streakProgress",
    )

    TillyCard(modifier = modifier) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // 제목 행: 텍스트 + 남은 일수
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "다음 연속 학습 보상까지",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = "${remaining}일",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            // 진행 바
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )

            // 마일스톤 라벨
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                val prevMilestone = MILESTONES.lastOrNull { it <= streakCount } ?: 0
                Text(
                    text = "${prevMilestone}일",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = "${nextMilestone}일",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun StreakRewardCardPreview() {
    TillyTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            StreakRewardCard(
                streakCount = 10,
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}
