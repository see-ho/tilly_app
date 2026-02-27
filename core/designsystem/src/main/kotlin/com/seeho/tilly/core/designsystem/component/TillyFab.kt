package com.seeho.tilly.core.designsystem.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.seeho.tilly.core.designsystem.theme.TillyTheme

/**
 * 중앙 FAB — 오늘의 TIL 상태에 따라 디자인 전환
 * - TIL 없음: + 아이콘 + primary(초록) 색상
 * - TIL 있음: ✓ 아이콘 + 회색
 */
@Composable
fun TillyFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    hasTodayTil: Boolean = false,
) {
    // 상태에 따라 색상과 아이콘 분기
    val fabColor = if (hasTodayTil) {
        MaterialTheme.colorScheme.surfaceVariant
    } else {
        MaterialTheme.colorScheme.primary
    }
    val fabOnColor = if (hasTodayTil) {
        MaterialTheme.colorScheme.onSurfaceVariant
    } else {
        MaterialTheme.colorScheme.onPrimary
    }
    val fabIcon = if (hasTodayTil) Icons.Default.Check else Icons.Default.Add
    val fabDescription = if (hasTodayTil) "오늘의 TIL 보기" else "TIL 작성"
    val glowColor = fabColor.copy(alpha = 0.6f)
    
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Spacer(
            modifier = Modifier
                .size(68.dp)
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(glowColor, Color.Transparent),
                            center = center.copy(y = center.y + 4.dp.toPx()),
                            radius = size.width / 2
                        ),
                        radius = size.width / 2,
                        center = center.copy(y = center.y + 4.dp.toPx())
                    )
                }
        )
        
        FloatingActionButton(
            onClick = onClick,
            modifier = Modifier.size(56.dp),
            containerColor = fabColor,
            contentColor = fabOnColor,
            shape = CircleShape,
            elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp)
        ) {
            Icon(
                imageVector = fabIcon,
                contentDescription = fabDescription,
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TillyFabPreview() {
    TillyTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Box(modifier = Modifier.padding(16.dp)) {
                TillyFab(onClick = {}, hasTodayTil = false)
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TillyFabCompletedPreview() {
    TillyTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Box(modifier = Modifier.padding(16.dp)) {
                TillyFab(onClick = {}, hasTodayTil = true)
            }
        }
    }
}
