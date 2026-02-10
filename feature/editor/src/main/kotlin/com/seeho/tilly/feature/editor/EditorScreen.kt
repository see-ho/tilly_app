package com.seeho.tilly.feature.editor

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seeho.tilly.core.designsystem.component.TillyTopAppBar
import com.seeho.tilly.core.designsystem.theme.TillyTheme
import com.seeho.tilly.feature.editor.component.CodeLineNumberTextField
import com.seeho.tilly.feature.editor.component.EditorSection
import com.seeho.tilly.feature.editor.component.TitleTextField

@Composable
fun EditorScreen(
    viewModel: EditorViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // 저장 성공 이벤트 수신 → 화면 종료
    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                EditorEvent.SaveSuccess -> onBackClick()
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
        onBackClick = onBackClick,
        onSaveClick = viewModel::onSave,
    )
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
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
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
                    // 저장 버튼
                    Button(
                        onClick = onSaveClick,
                        enabled = isSaveEnabled,
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                        modifier = Modifier.height(40.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "저장",
                            style = MaterialTheme.typography.titleMedium,
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
                com.seeho.tilly.core.designsystem.component.TillyLoadingIndicator(
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
