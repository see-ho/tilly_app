package com.seeho.tilly.core.designsystem.component

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seeho.tilly.core.designsystem.theme.EmotionAchievement
import com.seeho.tilly.core.designsystem.theme.EmotionFrustration
import com.seeho.tilly.core.designsystem.theme.TillyTheme

/**
 * 감정 라벨 뱃지
 */
@Composable
fun EmotionLabelBadge(
    label: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Text(
        text = label,
        style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.SemiBold,
        ),
        color = color,
        modifier = modifier
            .border(
                width = 1.5.dp,
                color = color,
                shape = CutCornerShape(4.dp),
            )
            .padding(horizontal = 8.dp, vertical = 2.dp),
    )
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun EmotionLabelBadgePreview() {
    TillyTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                EmotionLabelBadge(label = "좌절", color = EmotionFrustration)
                EmotionLabelBadge(label = "성취감", color = EmotionAchievement)
            }
        }
    }
}
