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
import com.seeho.tilly.core.designsystem.component.EmotionScoreIndicator
import com.seeho.tilly.core.designsystem.component.TillyCard
import com.seeho.tilly.core.designsystem.component.TillyTag

@Composable
fun TilFeedItem(
    title: String,
    emotionScore: Int,
    tags: List<String>,
    content: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val difficultyRes = when (emotionScore) {
        1, 2 -> com.seeho.tilly.core.designsystem.R.drawable.ic_difficult_easy
        3 -> com.seeho.tilly.core.designsystem.R.drawable.ic_difficult_normal
        4 -> com.seeho.tilly.core.designsystem.R.drawable.ic_difficult_hard
        5 -> com.seeho.tilly.core.designsystem.R.drawable.ic_difficult_veryhard
        else -> com.seeho.tilly.core.designsystem.R.drawable.ic_difficult_normal
    }

    TillyCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 난이도 아이콘
            androidx.compose.foundation.Image(
                bitmap = ImageBitmap.imageResource(id = difficultyRes),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                filterQuality = FilterQuality.None
            )

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

                EmotionScoreIndicator(score = emotionScore)

            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TilEntryItemPreview() {
    com.seeho.tilly.core.designsystem.theme.TillyTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            androidx.compose.foundation.layout.Box(modifier = Modifier.padding(16.dp)) {
                TilFeedItem(
                    title = "Preview TIL Title",
                    emotionScore = 4,
                    tags = listOf("jetpack", "compose", "ui"),
                    content = "This is a preview content for the TIL entry item. It shows how the title and tags are rendered.",
                    onClick = {}
                )
            }
        }
    }
}
