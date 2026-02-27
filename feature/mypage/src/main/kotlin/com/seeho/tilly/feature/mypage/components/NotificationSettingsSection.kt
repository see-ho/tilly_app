package com.seeho.tilly.feature.mypage.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seeho.tilly.core.designsystem.component.TillyCard
import com.seeho.tilly.core.designsystem.theme.TillyTheme
import com.seeho.tilly.core.model.NotificationSettings

/**
 * 알림 설정 섹션
 * 리마인더 / 계획 알람
 * 각각 ON/OFF 토글 + 시간 설정
 */
@Composable
fun NotificationSettingsSection(
    settings: NotificationSettings,
    onSettingsChange: (NotificationSettings) -> Unit,
    modifier: Modifier = Modifier,
) {
    // 현재 TimePicker를 표시할 알림 종류 (null이면 표시 안 함)
    var showTimePickerFor by remember { mutableStateOf<NotificationType?>(null) }

    Column(modifier = modifier) {
        Text(
            text = "알림 설정",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp),
        )

        TillyCard {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                // 리마인더 알림
                NotificationRow(
                    title = "리마인더",
                    description = "감정에 따라 격려 멘트도 함께 보내줘요",
                    enabled = settings.reminderEnabled,
                    hour = settings.reminderHour,
                    minute = settings.reminderMinute,
                    onToggle = { enabled ->
                        onSettingsChange(settings.copy(reminderEnabled = enabled))
                    },
                    onTimeClick = { showTimePickerFor = NotificationType.REMINDER },
                )

                // 계획 알람
                NotificationRow(
                    title = "계획 알람",
                    description = "전날 작성한 내일의 계획을 알려줘요",
                    enabled = settings.planEnabled,
                    hour = settings.planHour,
                    minute = settings.planMinute,
                    onToggle = { enabled ->
                        onSettingsChange(settings.copy(planEnabled = enabled))
                    },
                    onTimeClick = { showTimePickerFor = NotificationType.PLAN },
                )

            }
        }
    }

    // TimePicker 다이얼로그
    showTimePickerFor?.let { type ->
        val initialHour = when (type) {
            NotificationType.REMINDER -> settings.reminderHour
            NotificationType.PLAN -> settings.planHour
        }
        val initialMinute = when (type) {
            NotificationType.REMINDER -> settings.reminderMinute
            NotificationType.PLAN -> settings.planMinute
        }

        TimePickerDialog(
            initialHour = initialHour,
            initialMinute = initialMinute,
            onConfirm = { hour, minute ->
                val updated = when (type) {
                    NotificationType.REMINDER -> settings.copy(
                        reminderHour = hour,
                        reminderMinute = minute,
                    )
                    NotificationType.PLAN -> settings.copy(
                        planHour = hour,
                        planMinute = minute,
                    )
                }
                onSettingsChange(updated)
                showTimePickerFor = null
            },
            onDismiss = { showTimePickerFor = null },
        )
    }
}

/**
 * 알림 종류 enum
 */
private enum class NotificationType {
    REMINDER, PLAN
}

/**
 * 개별 알림 설정 Row
 */
@Composable
private fun NotificationRow(
    title: String,
    description: String,
    enabled: Boolean,
    hour: Int,
    minute: Int,
    onToggle: (Boolean) -> Unit,
    onTimeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // 제목 + 설명
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp),
            )
        }

        // 시간 표시 (활성화 시에만)
        if (enabled) {
            Text(
                text = String.format("%02d:%02d", hour, minute),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clickable(onClick = onTimeClick)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            )
        }

        // ON/OFF 토글
        Switch(
            checked = enabled,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                uncheckedThumbColor = MaterialTheme.colorScheme.onSurfaceVariant,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
        )
    }
}

/**
 * TimePicker 다이얼로그
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerDialog(
    initialHour: Int,
    initialMinute: Int,
    onConfirm: (hour: Int, minute: Int) -> Unit,
    onDismiss: () -> Unit,
) {
    val timePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = true,
    )

    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "알림 시간 설정",
                style = MaterialTheme.typography.titleMedium,
            )
        },
        text = {
            TimeInput(state = timePickerState)
        },
        confirmButton = {
            Text(
                text = "확인",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clickable {
                        onConfirm(timePickerState.hour, timePickerState.minute)
                    }
                    .padding(8.dp),
            )
        },
        dismissButton = {
            Text(
                text = "취소",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .clickable(onClick = onDismiss)
                    .padding(8.dp),
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
    )
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NotificationSettingsSectionPreview() {
    TillyTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            NotificationSettingsSection(
                settings = NotificationSettings(
                    reminderEnabled = true,
                    reminderHour = 20,
                    reminderMinute = 0,
                    planEnabled = false,
                ),
                onSettingsChange = {},
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}
