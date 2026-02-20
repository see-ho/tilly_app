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

// 초반 마일스톤 (세밀한 단계)
private val EARLY_MILESTONES = listOf(7, 14, 30)

// 이후 10일 단위 간격
private const val INTERVAL = 10

/**
 * 마일스톤 진행 정보
 */
private data class MilestoneProgress(val prev: Int, val next: Int, val progress: Float)

/**
 * 다음 마일스톤과 진행률 계산
 * 초반: 7 → 14 → 30일, 이후: 10일 단위 (40, 50, 60, ...)
 */
private fun getMilestoneProgress(streakCount: Int): MilestoneProgress {
    // 초반 마일스톤에서 다음 목표 찾기
    val nextMilestone = EARLY_MILESTONES.firstOrNull { it > streakCount }
        ?: run {
            // 30일 이후: 10단위로 올림 (31→40, 40→50, ...)
            val lastEarly = EARLY_MILESTONES.last()
            val overCount = streakCount - lastEarly
            lastEarly + ((overCount / INTERVAL) + 1) * INTERVAL
        }
    val prevMilestone = EARLY_MILESTONES.lastOrNull { it <= streakCount }
        ?: 0
    // 30일 넘은 경우 이전 마일스톤도 10단위로 계산
    val adjustedPrev = if (streakCount >= EARLY_MILESTONES.last()) {
        nextMilestone - INTERVAL
    } else {
        prevMilestone
    }
    val progress = if (nextMilestone == adjustedPrev) 1f
    else (streakCount - adjustedPrev).toFloat() / (nextMilestone - adjustedPrev).toFloat()
    return MilestoneProgress(adjustedPrev, nextMilestone, progress.coerceIn(0f, 1f))
}

/**
 * 다음 연속 학습 보상까지 진행 바 카드 (풀 너비)
 */
@Composable
fun StreakRewardCard(
    streakCount: Int,
    modifier: Modifier = Modifier,
) {
    val milestone = getMilestoneProgress(streakCount)
    val remaining = milestone.next - streakCount

    val animatedProgress by animateFloatAsState(
        targetValue = milestone.progress,
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
                Text(
                    text = "${milestone.prev}일",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = "${milestone.next}일",
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
