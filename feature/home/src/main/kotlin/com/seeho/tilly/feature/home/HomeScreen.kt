package com.seeho.tilly.feature.home

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.seeho.tilly.core.designsystem.component.TillyFab
import com.seeho.tilly.core.designsystem.theme.TillyTheme
import com.seeho.tilly.core.model.TilEntry
import com.seeho.tilly.core.model.mock.sampleTilEntries
import com.seeho.tilly.feature.home.components.TilFeed

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onTilClick: (Long) -> Unit,
    onEditorClick: () -> Unit,
    onShopClick: () -> Unit,
) {
    // ViewModel이나 다른 상태 관리 로직은 여기서 처리 (Stateful)
    val sampleEntries = remember { sampleTilEntries }

    HomeContent(
        entries = sampleEntries,
        onTilClick = onTilClick,
        onEditorClick = onEditorClick,
        onShopClick = onShopClick
    )
}

@Composable
fun HomeContent(
    entries: List<TilEntry>,
    onTilClick: (Long) -> Unit,
    onEditorClick: () -> Unit,
    onShopClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // UI 렌더링만 담당 (Stateless)
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            TillyFab(onClick = onEditorClick)
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->
        androidx.compose.foundation.layout.Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            TilFeed(
                entries = entries,
                onTilClick = onTilClick,
                onShopClick = onShopClick,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun HomeContentPreview() {
    val sampleEntries = sampleTilEntries
    TillyTheme {
        HomeContent(
            entries = sampleEntries,
            onTilClick = {},
            onEditorClick = {},
            onShopClick = {}
        )
    }
}
