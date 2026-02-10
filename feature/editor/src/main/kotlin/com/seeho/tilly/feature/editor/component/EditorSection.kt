package com.seeho.tilly.feature.editor.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 에디터 섹션 컴포넌트
 * 코드 주석 스타일 라벨(// label)과 필수 표시(*)를 포함하는 섹션 래퍼
 */
@Composable
fun EditorSection(
    label: String,
    modifier: Modifier = Modifier,
    isRequired: Boolean = false,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // 라벨 행: "// label" + 필수 표시
        Row {
            Text(
                text = "// $label",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (isRequired) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "*",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 컨텐츠 슬롯
        content()
    }
}
