package com.seeho.tilly.feature.editor.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seeho.tilly.core.designsystem.theme.JetBrainsMonoFontFamily

/**
 * 코드 에디터 스타일 텍스트 필드
 * - onTextLayout으로 실제 줄 수를 감지하여 줄번호 동기화
 * - 공유 ScrollState로 스크롤 동기화
 */
@Composable
fun CodeLineNumberTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    minLines: Int = 5,
    maxHeight: Dp = 280.dp,
) {
    // 실제 렌더링된 줄 수 (onTextLayout에서 업데이트)
    var actualLineCount by remember { mutableIntStateOf(minLines) }

    // 줄번호 패널과 텍스트 필드가 공유하는 스크롤 상태
    val scrollState = rememberScrollState()

    val outlineColor = MaterialTheme.colorScheme.outline
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface

    // 줄번호 텍스트 스타일
    val lineNumberStyle = remember(outlineColor) {
        TextStyle(
            fontFamily = JetBrainsMonoFontFamily,
            fontSize = 14.sp,
            lineHeight = 24.sp,
            color = outlineColor,
        )
    }

    // 본문 텍스트 스타일
    val textStyle = remember(onSurfaceColor) {
        TextStyle(
            fontFamily = JetBrainsMonoFontFamily,
            fontSize = 14.sp,
            lineHeight = 24.sp,
            color = onSurfaceColor,
        )
    }

    // 표시할 줄 수 (실제 줄 수와 최소 줄 수 중 큰 값)
    val displayLineCount = maxOf(actualLineCount, minLines)


    var previousLineCount by remember { mutableIntStateOf(minLines) }
    LaunchedEffect(actualLineCount) {
        if (actualLineCount > previousLineCount && actualLineCount > minLines) {
            scrollState.animateScrollTo(scrollState.maxValue)
        }
        previousLineCount = actualLineCount
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = maxHeight)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.medium,
            )
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.medium,
            ),
    ) {
        // 줄번호 패널 (텍스트 필드와 동일한 scrollState 공유)
        Box(
            modifier = Modifier
                .width(40.dp)
                .fillMaxHeight()
                .verticalScroll(scrollState)
                .padding(top = 12.dp, bottom = 12.dp, end = 8.dp),
        ) {
            val lineNumbers = (1..displayLineCount).joinToString("\n") { it.toString() }
            Text(
                text = lineNumbers,
                style = lineNumberStyle,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
            )
        }

        // 구분선
        Box(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.outline),
        )

        // 텍스트 입력 영역
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            // placeholder 텍스트
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    style = textStyle.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    ),
                )
            }

            // 실제 텍스트 입력 필드
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = textStyle,
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                modifier = Modifier.fillMaxWidth(),
                // onTextLayout으로 실제 렌더링된 줄 수 감지
                onTextLayout = { textLayoutResult ->
                    actualLineCount = textLayoutResult.lineCount
                },
            )
        }
    }
}
