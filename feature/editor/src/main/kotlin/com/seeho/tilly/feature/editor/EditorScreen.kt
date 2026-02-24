package com.seeho.tilly.feature.editor

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardActions
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seeho.tilly.core.designsystem.component.CoinRewardDialog
import com.seeho.tilly.core.designsystem.component.TillyLoadingIndicator
import com.seeho.tilly.core.designsystem.component.TillySnackbarHost
import com.seeho.tilly.core.designsystem.component.TillyTopAppBar
import com.seeho.tilly.core.designsystem.theme.TillyTheme
import com.seeho.tilly.feature.editor.component.CodeLineNumberTextField
import com.seeho.tilly.feature.editor.component.EditorSection
import com.seeho.tilly.feature.editor.component.TitleTextField
import com.seeho.tilly.core.designsystem.component.TillyAlertDialog
import com.seeho.tilly.core.designsystem.R as designR

@Composable
fun EditorScreen(
    viewModel: EditorViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onShowDetail: (Long) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val coinRewardEvent by viewModel.coinRewardEvent.collectAsStateWithLifecycle()

    // 작성 중인 내용이 있는지 판별
    val hasContent = uiState.title.isNotBlank() || uiState.todayLearning.isNotBlank()
            || uiState.difficulties.isNotBlank() || uiState.tomorrowPlan.isNotBlank()

    // 나가기 확인 다이얼로그 상태
    var showExitDialog by remember { mutableStateOf(false) }

    // 뒤로가기 가드: 내용이 있으면 다이얼로그, 없으면 바로 나감
    val guardedBack: () -> Unit = {
        if (hasContent) {
            showExitDialog = true
        } else {
            onBackClick()
        }
    }

    // 시스템 뒤로가기 버튼도 동일하게 처리
    BackHandler(enabled = hasContent) {
        showExitDialog = true
    }

    // 나가기 확인 다이얼로그
    if (showExitDialog) {
        TillyAlertDialog(
            onDismissRequest = { showExitDialog = false },
            onConfirm = {
                showExitDialog = false
                onBackClick()
            },
            title = "작성 중인 내용이 있어요",
            text = "지금 나가면 작성 중인 내용이 사라져요.\n정말 나가시겠어요?",
            confirmText = "나가기",
            dismissText = "계속 작성",
        )
    }

    // 이벤트 수신 → 저장 성공 시 상세 화면 이동, 실패 시 스낵바 표시
    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is EditorEvent.SaveSuccess -> onShowDetail(event.tilId)
                is EditorEvent.SaveFailed -> {
                    snackbarHostState.showSnackbar("저장에 실패했습니다. 다시 시도해주세요.")
                }
                is EditorEvent.AnalysisLimitReached -> {
                    snackbarHostState.showSnackbar("오늘 무료 분석 횟수를 모두 사용했어요. 코인이 부족합니다.")
                }
                is EditorEvent.ReanalysisSuccess -> {
                    snackbarHostState.showSnackbar("✨ 재분석 완료! 저장하면 결과가 반영돼요")
                }
            }
        }
    }

    // 로딩 중 (수정 모드에서 기존 데이터 불러오는 중)
    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        return
    }

    // EditorContent + 코인 보상 다이얼로그 오버레이
    Box(modifier = Modifier.fillMaxSize()) {
        EditorContent(
            title = uiState.title,
            onTitleChange = viewModel::onTitleChange,
            todayLearning = uiState.todayLearning,
            onTodayLearningChange = viewModel::onTodayLearningChange,
            difficulties = uiState.difficulties,
            onDifficultiesChange = viewModel::onDifficultiesChange,
            tomorrowPlan = uiState.tomorrowPlan,
            onTomorrowPlanChange = viewModel::onTomorrowPlanChange,
            isSaveEnabled = uiState.isSaveEnabled,
            isEditMode = uiState.isEditMode,
            isSaving = uiState.isSaving,
            isAnalyzing = uiState.isAnalyzing,
            remainingFreeAnalysis = uiState.remainingFreeAnalysis,
            coinBalance = uiState.coinBalance,
            onBackClick = guardedBack,
            onSaveClick = viewModel::onSave,
            onReanalyzeClick = viewModel::onReanalyzeClick,
            snackbarHostState = snackbarHostState,
        )

        // 코인 보상 다이얼로그
        CoinRewardDialog(
            visible = coinRewardEvent != null,
            rewardResult = coinRewardEvent,
            onDismiss = viewModel::consumeCoinRewardEvent,
        )

        // 유료 분석 확인 다이얼로그
        if (uiState.showPaidAnalysisDialog) {
            TillyAlertDialog(
                onDismissRequest = viewModel::dismissPaidAnalysisDialog,
                onConfirm = viewModel::confirmPaidAnalysis,
                title = "AI 재분석",
                text = "무료 분석 횟수를 모두 사용했어요.\n30코인으로 재분석할까요?\n(보유: ${uiState.coinBalance}코인)",
                confirmText = "분석하기",
                dismissText = "취소",
                confirmButtonColor = MaterialTheme.colorScheme.primary,
            )
        }

        // 오프라인 저장 경고 다이얼로그
        if (uiState.showOfflineSaveDialog) {
            TillyAlertDialog(
                onDismissRequest = viewModel::dismissOfflineSaveDialog,
                onConfirm = viewModel::confirmOfflineSave,
                title = "네트워크 연결 없음",
                text = "네트워크가 연결되지 않아 AI 분석 없이 저장됩니다.\n저장 후 재분석으로 AI 피드백을 받을 수 있어요.",
                confirmText = "저장하기",
                dismissText = "취소",
                confirmButtonColor = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
fun EditorContent(
    title: String,
    onTitleChange: (String) -> Unit,
    todayLearning: String,
    onTodayLearningChange: (String) -> Unit,
    difficulties: String,
    onDifficultiesChange: (String) -> Unit,
    tomorrowPlan: String,
    onTomorrowPlanChange: (String) -> Unit,
    isSaveEnabled: Boolean,
    isEditMode: Boolean,
    isSaving: Boolean,
    isAnalyzing: Boolean,
    remainingFreeAnalysis: Int = 3,
    coinBalance: Int = 0,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onReanalyzeClick: () -> Unit = {},
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    modifier: Modifier = Modifier,
) {
    // 키보드 제어 및 포커스 관리
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val titleFocus = remember { FocusRequester() }
    val learningFocus = remember { FocusRequester() }
    val difficultyFocus = remember { FocusRequester() }
    val tomorrowFocus = remember { FocusRequester() }
    Scaffold(
        modifier = modifier,
        snackbarHost = {
            TillySnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TillyTopAppBar(
                titleText = if (isEditMode) "TIL 수정" else "TIL 작성",
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "뒤로가기",
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                },
                actions = {
                    // 수정 모드: 재분석 아이콘 버튼
                    if (isEditMode) {
                        Box {
                            IconButton(
                                onClick = onReanalyzeClick,
                                enabled = !isAnalyzing && !isSaving,
                            ) {
                                if (isAnalyzing) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        strokeWidth = 2.dp,
                                        color = MaterialTheme.colorScheme.onSurface,
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "재분석",
                                        tint = MaterialTheme.colorScheme.onSurface,
                                    )
                                }
                            }
                            // 남은 횟수 뱃지
                            if (!isAnalyzing) {
                                if (remainingFreeAnalysis > 0) {
                                    Text(
                                        text = "$remainingFreeAnalysis",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(top = 6.dp, end = 6.dp),
                                    )
                                } else {
                                    Image(
                                        painter = painterResource(id = designR.drawable.ic_coin),
                                        contentDescription = "코인 필요",
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(top = 6.dp, end = 6.dp)
                                            .size(14.dp),
                                    )
                                }
                            }
                        }
                    }
                    // 저장 아이콘 버튼 (키보드 내림 후 저장)
                    IconButton(
                        onClick = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                            onSaveClick()
                        },
                        enabled = isSaveEnabled,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = "저장",
                            tint = if (isSaveEnabled) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                            },
                        )
                    }
                },
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // 제목
            EditorSection(label = "제목", isRequired = true) {
                TitleTextField(
                    value = title,
                    onValueChange = onTitleChange,
                    placeholder = "오늘의 학습 제목을 입력하세요...",
                    modifier = Modifier.focusRequester(titleFocus),
                    keyboardActions = KeyboardActions(
                        onNext = { learningFocus.requestFocus() }
                    ),
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 오늘 배운 것 섹션 (필수)
            EditorSection(label = "오늘 배운 것", isRequired = true) {
                CodeLineNumberTextField(
                    value = todayLearning,
                    onValueChange = onTodayLearningChange,
                    placeholder = "오늘 배운 내용을 작성하세요...",
                    minLines = 10,
                    maxHeight = 280.dp,
                    focusRequester = learningFocus,
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 어려웠던 점 섹션 (선택)
            EditorSection(label = "어려웠던 점 (선택)") {
                CodeLineNumberTextField(
                    value = difficulties,
                    onValueChange = onDifficultiesChange,
                    placeholder = "어려웠던 점이나 도전했던 내용을 작성하세요...",
                    minLines = 5,
                    maxHeight = 146.dp,
                    focusRequester = difficultyFocus,
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 내일 할 일 섹션 (선택)
            EditorSection(label = "내일 할 일 (선택)") {
                CodeLineNumberTextField(
                    value = tomorrowPlan,
                    onValueChange = onTomorrowPlanChange,
                    placeholder = "내일 공부할 내용이나 계획을 작성하세요...",
                    minLines = 5,
                    maxHeight = 146.dp,
                    focusRequester = tomorrowFocus,
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 안내 문구
            Text(
                text = "// '저장' 버튼을 눌러 TIL을 저장하고 AI 분석을 받으세요",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(32.dp))
        }

        // 저장 또는 분석 중일 때 전체 화면 로딩 오버레이
        if (isSaving || isAnalyzing) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center,
            ) {
                TillyLoadingIndicator(
                    text = if (isAnalyzing) "틸리가 배운 내용을 분석하고 있어요..." else "저장 중..."
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showSystemUi = true,
)
@Composable
private fun EditorContentPreview() {
    TillyTheme {
        EditorContent(
            title = "",
            onTitleChange = {},
            todayLearning = "",
            onTodayLearningChange = {},
            difficulties = "",
            onDifficultiesChange = {},
            tomorrowPlan = "",
            onTomorrowPlanChange = {},
            isSaveEnabled = false,
            isEditMode = false,
            isSaving = false,
            isAnalyzing = false,
            onBackClick = {},
            onSaveClick = {},
        )
    }
}
