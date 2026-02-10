package com.seeho.tilly.feature.editor

import android.content.res.Configuration
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seeho.tilly.core.designsystem.component.TillyTopAppBar
import com.seeho.tilly.core.designsystem.theme.TillyTheme
import com.seeho.tilly.feature.editor.component.CodeLineNumberTextField
import com.seeho.tilly.feature.editor.component.EditorSection
import com.seeho.tilly.feature.editor.component.TitleTextField

@Composable
fun EditorScreen(
    onBackClick: () -> Unit = {},
) {
    // 각 섹션의 입력 상태
    var title by rememberSaveable { mutableStateOf("") }
    var todayLearning by rememberSaveable { mutableStateOf("") }
    var difficulties by rememberSaveable { mutableStateOf("") }
    var tomorrowPlan by rememberSaveable { mutableStateOf("") }

    // 저장 가능 여부: 제목과 오늘 배운 것이 비어있지 않을 때
    val isSaveEnabled = title.isNotBlank() && todayLearning.isNotBlank()

    EditorContent(
        title = title,
        onTitleChange = { title = it },
        todayLearning = todayLearning,
        onTodayLearningChange = { todayLearning = it },
        difficulties = difficulties,
        onDifficultiesChange = { difficulties = it },
        tomorrowPlan = tomorrowPlan,
        onTomorrowPlanChange = { tomorrowPlan = it },
        isSaveEnabled = isSaveEnabled,
        onBackClick = onBackClick,
        onSaveClick = {
            // TODO: ViewModel을 통한 저장 로직 연결
        },
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
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TillyTopAppBar(
                titleText = "TIL 작성",
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
            onBackClick = {},
            onSaveClick = {},
        )
    }
}
