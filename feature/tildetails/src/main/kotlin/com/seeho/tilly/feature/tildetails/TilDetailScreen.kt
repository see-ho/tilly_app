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
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seeho.tilly.core.designsystem.component.TillyAlertDialog
import com.seeho.tilly.core.designsystem.component.TillyLoadingIndicator
import com.seeho.tilly.core.designsystem.component.TillySnackbarHost
import com.seeho.tilly.core.designsystem.component.TillyTopAppBar
import com.seeho.tilly.core.designsystem.theme.TillyTheme
import com.seeho.tilly.core.model.Difficulty
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
    fromSave: Boolean = false,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // 저장 후 진입 시 스낵바 표시
    LaunchedEffect(fromSave) {
        if (fromSave) {
            snackbarHostState.showSnackbar("TIL이 저장되었습니다!")
        }
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // 삭제 성공 이벤트 수신 → 화면 종료
    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                TilDetailEvent.DeleteSuccess -> onDeleteClick()
                TilDetailEvent.DeleteFailed -> {
                    snackbarHostState.showSnackbar("삭제에 실패했습니다. 다시 시도해주세요.")
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
                snackbarHostState = snackbarHostState,
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
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    // 삭제 확인 다이얼로그 표시 상태
    var showDeleteDialog by remember { mutableStateOf(false) }

    // 삭제 확인 다이얼로그
    if (showDeleteDialog) {
        TillyAlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            onConfirm = {
                showDeleteDialog = false
                onDeleteClick()
            },
            title = "TIL 삭제",
            text = "이 TIL을 정말 삭제하시겠습니까?\n삭제된 TIL은 복구할 수 없습니다.",
            confirmText = "삭제",
            dismissText = "취소",
        )
    }

    Scaffold(
        snackbarHost = {
            TillySnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TillyTopAppBar(
                titleText = "TIL 상세",
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "뒤로가기",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                actions = {
                    // 편집 버튼
                    IconButton(onClick = onEditClick) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "수정",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    // 삭제 버튼 - 다이얼로그로 확인 후 삭제
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "삭제",
                            tint = MaterialTheme.colorScheme.error
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
                title = "오늘 배운 것",
                content = til.learned,
                icon = Icons.Default.Info
            )

            til.difficulty?.let {
                Spacer(modifier = Modifier.height(24.dp))
                TilDetailSection(
                    title = "어려웠던 점",
                    content = it,
                    icon = Icons.Default.ErrorOutline
                )
            }

            til.tomorrow?.let {
                Spacer(modifier = Modifier.height(24.dp))
                TilDetailSection(
                    title = "내일 할 일",
                    content = it,
                    icon = Icons.Default.CalendarToday
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
        difficultyLevel = Difficulty.HARD,
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
