package com.seeho.tilly.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seeho.tilly.core.designsystem.component.TillyFab
import com.seeho.tilly.core.designsystem.component.TillyLoadingIndicator
import com.seeho.tilly.core.designsystem.theme.TillyTheme
import com.seeho.tilly.core.model.Til
import com.seeho.tilly.feature.home.components.TilFeed

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onTilClick: (Long) -> Unit,
    onEditorClick: () -> Unit,
    onShopClick: () -> Unit,
) {
    // ViewModel에서 UI 상태 수집
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val deletingTilId by viewModel.deletingTilId.collectAsStateWithLifecycle()

    HomeContent(
        uiState = uiState,
        onTilClick = onTilClick,
        onDeleteClick = viewModel::showDeleteDialog,
        onEditorClick = onEditorClick,
        onShopClick = onShopClick,
    )

    if (deletingTilId != null) {
        com.seeho.tilly.core.designsystem.component.TillyAlertDialog(
            onDismissRequest = viewModel::dismissDeleteDialog,
            onConfirm = viewModel::deleteTil,
            title = "TIL 삭제",
            text = "정말로 이 TIL을 삭제하시겠습니까? 삭제된 내용은 복구할 수 없습니다.",
            confirmText = "삭제",
            dismissText = "취소"
        )
    }
}

@Composable
fun HomeContent(
    uiState: HomeUiState,
    onTilClick: (Long) -> Unit,
    onDeleteClick: (Long) -> Unit,
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
        when (uiState) {
            is HomeUiState.Loading -> {
                // 로딩 인디케이터
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center,
                ) {
                    TillyLoadingIndicator()
                }
            }
            is HomeUiState.Empty -> {
                // 빈 상태: TIL이 없을 때
                TilFeed(
                    tils = emptyList(),
                    onTilClick = onTilClick,
                    onDelete = onDeleteClick,
                    onShopClick = onShopClick,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                )
            }
            is HomeUiState.Success -> {
                TilFeed(
                    tils = uiState.tils,
                    onTilClick = onTilClick,
                    onDelete = onDeleteClick,
                    onShopClick = onShopClick,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                )
            }

            is HomeUiState.Error -> {
                // TODO 에러 상태: TIL이 없을 때와 비슷하게 처리하거나 별도 UI 표시
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = uiState.message ?: "알 수 없는 오류가 발생했습니다.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun HomeContentPreview() {
    val sampleTils = listOf(
        Til(
            id = 1L,
            title = "Jetpack Compose Animation",
            learned = "Today I learned how to use Animatable...",
            tags = listOf("compose", "animation"),
            emotionScore = 5,
            createdAt = System.currentTimeMillis(),
        ),
    )
    TillyTheme {
        HomeContent(
            uiState = HomeUiState.Success(sampleTils),
            onTilClick = {},
            onDeleteClick = {},
            onEditorClick = {},
            onShopClick = {},
        )
    }
}
