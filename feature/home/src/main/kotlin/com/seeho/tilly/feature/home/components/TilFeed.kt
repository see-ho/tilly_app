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
import com.seeho.tilly.core.model.Til
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TilFeed(
    tils: List<Til>,
    onTilClick: (Long) -> Unit,
    onShopClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // 날짜 포맷터 (timestamp → "2026.02.10" 형태)
    val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())

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

        // 3. TIL 리스트
        items(
            items = tils,
            key = { it.id }
        ) { til ->
            Column {
                Text(
                    text = dateFormat.format(Date(til.createdAt)),
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
                    title = til.title,
                    emotionScore = til.emotionScore ?: 3,
                    tags = til.tags,
                    content = til.learned,
                    onClick = { onTilClick(til.id) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TilFeedPreview() {
    val sampleTils = listOf(
        Til(
            id = 1L,
            title = "Jetpack Compose Animation",
            learned = "Today I learned how to use Animatable to create smooth transitions...",
            tags = listOf("compose", "animation"),
            emotionScore = 5,
            createdAt = System.currentTimeMillis(),
        ),
    )
    TillyTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            TilFeed(
                tils = sampleTils,
                onTilClick = {},
                onShopClick = {},
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
