package com.seeho.tilly.feature.editor.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.seeho.tilly.core.designsystem.theme.JetBrainsMonoFontFamily

/**
 * 제목 입력용 단일 줄 텍스트 필드
 * 줄번호 없이 간단한 텍스트 입력
 */
@Composable
fun TitleTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
) {
    val textStyle = MaterialTheme.typography.bodyLarge.copy(
        fontFamily = JetBrainsMonoFontFamily,
        color = MaterialTheme.colorScheme.onSurface,
    )

    BasicTextField(
        value = value,
        onValueChange = { newValue ->
            // 제목은 단일 줄만 허용 (줄바꿈 제거)
            onValueChange(newValue.replace("\n", ""))
        },
        textStyle = textStyle,
        singleLine = true,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.medium,
            )
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.medium,
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        decorationBox = { innerTextField ->
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    style = textStyle.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    ),
                )
            }
            innerTextField()
        },
    )
}
