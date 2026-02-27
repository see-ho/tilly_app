package com.seeho.tilly.feature.home.components

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.style.TextOverflow
import com.seeho.tilly.core.designsystem.component.EmotionLabelBadge
import com.seeho.tilly.core.designsystem.component.EmotionScoreIndicator
import com.seeho.tilly.core.designsystem.component.TillyCard
import com.seeho.tilly.core.designsystem.component.TillyTag
import com.seeho.tilly.core.designsystem.theme.TillyTheme
import com.seeho.tilly.core.designsystem.util.color
import com.seeho.tilly.core.designsystem.util.iconRes
import com.seeho.tilly.core.model.Difficulty
import com.seeho.tilly.core.model.Emotion

@Composable
fun TilFeedItem(
    title: String,
    emotionScore: Int?,
    emotion: Emotion?,
    difficultyLevel: Difficulty?,
    tags: List<String>,
    content: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // 난이도 아이콘 (분석 안 됐으면 null)
    val difficultyRes = difficultyLevel?.iconRes

    TillyCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 난이도 아이콘 (분석 있을 때만 표시)
            if (difficultyRes != null) {
                androidx.compose.foundation.Image(
                    bitmap = ImageBitmap.imageResource(id = difficultyRes),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    filterQuality = FilterQuality.None
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (tags.isNotEmpty()) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(tags) { tag ->
                            TillyTag(text = tag)
                        }
                    }
                }
                
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                // 감정 인디케이터 + 라벨 뱃지 (분석 있을 때만 표시)
                if (emotionScore != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        EmotionScoreIndicator(score = emotionScore)
                        if (emotion != null) {
                            EmotionLabelBadge(
                                label = emotion.label,
                                color = emotion.color,
                            )
                        }
                    }
                }

            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TilEntryItemPreview() {
    TillyTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            androidx.compose.foundation.layout.Box(modifier = Modifier.padding(16.dp)) {
                TilFeedItem(
                    title = "Preview TIL Title",
                    emotionScore = 4,
                    emotion = Emotion.ACHIEVEMENT,
                    difficultyLevel = Difficulty.HARD,
                    tags = listOf("jetpack", "compose", "ui"),
                    content = "This is a preview content for the TIL entry item.",
                    onClick = {}
                )
            }
        }
    }
}
