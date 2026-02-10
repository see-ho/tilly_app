package com.seeho.tilly.feature.tildetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seeho.tilly.core.designsystem.component.TillyLoadingIndicator
import com.seeho.tilly.core.designsystem.component.TillyTopAppBar
import com.seeho.tilly.core.designsystem.theme.TillyTheme
import com.seeho.tilly.core.model.Til
import com.seeho.tilly.feature.tildetails.components.TilDetailCommentCard
import com.seeho.tilly.feature.tildetails.components.TilDetailHeader
import com.seeho.tilly.feature.tildetails.components.TilDetailSection

@Composable
fun TilDetailScreen(
    viewModel: TilDetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onEditClick: (Long) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // 삭제 성공 이벤트 수신 → 화면 종료
    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                TilDetailEvent.DeleteSuccess -> onDeleteClick()
                TilDetailEvent.DeleteFailed -> {
                    // TODO: 삭제 실패 알림
                }
            }
        }
    }

    when (val state = uiState) {
        is TilDetailUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                TillyLoadingIndicator()
            }
        }
        is TilDetailUiState.NotFound -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "TIL을 찾을 수 없습니다",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        is TilDetailUiState.Success -> {
            TilDetailContent(
                til = state.til,
                onBackClick = onBackClick,
                onDeleteClick = viewModel::onDelete,
                onEditClick = { onEditClick(state.til.id) },
            )
        }
    }
}

@Composable
fun TilDetailContent(
    til: Til,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TillyTopAppBar(
                titleText = "TIL Details",
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                actions = {
                    // 편집 버튼
                    IconButton(onClick = onEditClick) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    // 삭제 버튼
                    IconButton(onClick = onDeleteClick) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                        )
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            TilDetailHeader(til = til)

            Spacer(modifier = Modifier.height(24.dp))

            TilDetailSection(
                title = "What I Learned",
                content = til.learned,
                icon = Icons.Default.Info
            )

            //TODO ICON 수정
            til.difficulty?.let {
                Spacer(modifier = Modifier.height(24.dp))
                TilDetailSection(
                    title = "What Was Difficult",
                    content = it,
                    icon = Icons.Default.Warning
                )
            }

            //TODO ICON 수정
            til.tomorrow?.let {
                Spacer(modifier = Modifier.height(24.dp))
                TilDetailSection(
                    title = "Tomorrow's Plan",
                    content = it,
                    icon = Icons.Default.Edit
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            TilDetailCommentCard(
                til = til
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TilDetailScreenPreview() {
    val sampleTil = Til(
        id = 1L,
        title = "Jetpack Compose ViewModel로 상태 관리하기",
        learned = "오늘은 Jetpack Compose에서 ViewModel과 StateFlow를 사용해서 상태를 관리하는 방법을 배웠다.",
        difficulty = "MutableStateFlow와 MutableState를 언제 써야 하는지 헷갈렸다.",
        tomorrow = "내일은 더 복잡한 예제를 구현할 계획이다.",
        tags = listOf("jetpack compose", "viewmodel"),
        emotionScore = 4,
        difficultyLevel = "HARD",
        createdAt = System.currentTimeMillis(),
    )
    TillyTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            TilDetailContent(
                til = sampleTil,
                onBackClick = {},
                onDeleteClick = {},
                onEditClick = {},
            )
        }
    }
}
