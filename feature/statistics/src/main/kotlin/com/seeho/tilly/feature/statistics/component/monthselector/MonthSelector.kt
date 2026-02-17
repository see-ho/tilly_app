package com.seeho.tilly.feature.statistics.component.monthselector

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seeho.tilly.core.designsystem.theme.TillyShapes

/**
 * 월 선택기 컴포넌트
 * 좌/우 화살표로 월 이동, 가운데에 월/년도/TIL 개수 표시
 */
@Composable
fun MonthSelector(
    month: Int,
    year: Int,
    tilCount: Int,
    canGoNext: Boolean,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(TillyShapes.large)
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // 이전 달 버튼
        IconButton(
            onClick = onPreviousMonth,
            modifier = Modifier.size(36.dp),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.3f),
            ),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "이전 달",
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }

        // 월/년도/TIL 개수
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "${month}월",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 24.sp,
                    lineHeight = 32.sp,
                ),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
            )
            Text(
                text = "$year",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            Text(
                text = "${tilCount}개의 TIL",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
            )
        }

        // 다음 달 버튼
        IconButton(
            onClick = onNextMonth,
            modifier = Modifier
                .size(36.dp)
                .alpha(if (canGoNext) 1f else 0.3f),
            enabled = canGoNext,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "다음 달",
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}
