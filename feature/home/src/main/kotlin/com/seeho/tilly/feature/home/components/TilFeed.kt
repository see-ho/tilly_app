package com.seeho.tilly.feature.home.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.tooling.preview.Preview
import com.seeho.tilly.core.designsystem.theme.TillyTheme
import com.seeho.tilly.core.model.TilEntry
import com.seeho.tilly.core.model.mock.sampleTilEntries

@Composable
fun TilFeed(
    entries: List<TilEntry>,
    onTilClick: (Long) -> Unit,
    onShopClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(bottom = 16.dp),
    ) {
        // 1. Tilly 섹션
        item {
            HomeTillySection(onShopClick = onShopClick)
        }

        // 2. 리스트 헤더
        item {
            TilSectionHeader()
        }

        // 3. 날짜별 TIL 리스트
        items(
            items = entries,
            key = { it.id }
        ) { entry ->
            Column {
                Text(
                    text = entry.date,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 8.dp,
                        bottom = 4.dp
                    )
                )

                TilFeedItem(
                    title = entry.title,
                    emotionScore = entry.emotionScore,
                    tags = entry.tags,
                    content = entry.content,
                    onClick = { onTilClick(entry.id) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TilFeedPreview() {
    TillyTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            TilFeed(
                entries = sampleTilEntries,
                onTilClick = {},
                onShopClick = {},
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
