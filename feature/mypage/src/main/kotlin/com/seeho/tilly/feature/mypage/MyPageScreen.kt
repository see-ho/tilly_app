package com.seeho.tilly.feature.mypage

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seeho.tilly.core.designsystem.component.TillyLoadingIndicator
import com.seeho.tilly.core.designsystem.theme.TillyTheme
import com.seeho.tilly.core.model.NotificationSettings
import com.seeho.tilly.feature.mypage.components.ActivityStatsRow
import com.seeho.tilly.feature.mypage.components.AppInfoSection
import com.seeho.tilly.feature.mypage.components.CoinHistoryEntryRow
import com.seeho.tilly.feature.mypage.components.NotificationSettingsSection
import com.seeho.tilly.feature.mypage.components.ProfileSection
import com.seeho.tilly.feature.mypage.components.StreakRewardCard
import com.seeho.tilly.feature.mypage.components.WeeklyCheckCard

@Composable
fun MyPageScreen(
    viewModel: MyPageViewModel = hiltViewModel(),
    onOpenSourceClick: () -> Unit = {},
    onCoinHistoryClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // 알림 토글 ON 시 일시 저장할 설정값
    var pendingNotificationSettings by remember { mutableStateOf<NotificationSettings?>(null) }

    // POST_NOTIFICATIONS 권한 요청 런처 (Android 13+)
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        if (isGranted) {
            // 권한 승인 → 대기 중이던 설정 적용
            pendingNotificationSettings?.let { settings ->
                viewModel.updateNotificationSettings(settings)
            }
        }
        pendingNotificationSettings = null
    }

    MyPageContent(
        uiState = uiState,
        onNotificationSettingsChange = { newSettings ->
            // Android 13+ 알림 권한 확인
            val needsPermission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                (newSettings.reminderEnabled || newSettings.planEnabled) &&
                ContextCompat.checkSelfPermission(
                    context, Manifest.permission.POST_NOTIFICATIONS,
                ) != PackageManager.PERMISSION_GRANTED

            if (needsPermission) {
                // 권한 없으면 요청 후 대기
                pendingNotificationSettings = newSettings
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                // 권한 있으면 바로 적용
                viewModel.updateNotificationSettings(newSettings)
            }
        },
        onOpenSourceClick = onOpenSourceClick,
        onCoinHistoryClick = onCoinHistoryClick,
        modifier = modifier,
    )
}

@Composable
fun MyPageContent(
    uiState: MyPageUiState,
    onNotificationSettingsChange: (NotificationSettings) -> Unit,
    onOpenSourceClick: () -> Unit = {},
    onCoinHistoryClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    if (uiState.isLoading) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center,
        ) {
            TillyLoadingIndicator()
        }
        return
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
    ) {
        // 1. 프로필 섹션
        item {
            ProfileSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            )
        }

        // 2. Weekly Check
        item {
            WeeklyCheckCard(
                weeklyCheck = uiState.weeklyCheck,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
            )
        }

        // 3. 활동 통계 카드
        item {
            ActivityStatsRow(
                streakCount = uiState.streakCount,
                totalTilCount = uiState.totalTilCount,
                modifier = Modifier.padding(top = 12.dp),
            )
        }

        // 3-1. 다음 연속 학습 보상 진행 바
        item {
            StreakRewardCard(
                streakCount = uiState.streakCount,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            )
        }

        // 4. 코인 내역 진입
        item {
            CoinHistoryEntryRow(
                onClick = onCoinHistoryClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
            )
        }

        // 5. 알림 설정
        item {
            NotificationSettingsSection(
                settings = uiState.notificationSettings,
                onSettingsChange = onNotificationSettingsChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
            )
        }

        // 6. 앱 정보
        item {
            AppInfoSection(
                onOpenSourceClick = onOpenSourceClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
            )
        }

        // 버전 정보
        item {
            Text(
                text = "© 2026 Tilly · v${uiState.appVersion}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 32.dp),
            )
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun MyPageContentPreview() {
    TillyTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            MyPageContent(
                uiState = MyPageUiState(
                    weeklyCheck = listOf(true, true, false, false, false, false, false),
                    streakCount = 10,
                    totalTilCount = 7,
                    notificationSettings = NotificationSettings(
                        reminderEnabled = true,
                        reminderHour = 20,
                    ),
                    appVersion = "1.0.0",
                ),
                onNotificationSettingsChange = {},
                onOpenSourceClick = {},
            )
        }
    }
}
