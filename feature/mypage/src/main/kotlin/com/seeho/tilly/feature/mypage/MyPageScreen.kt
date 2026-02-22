package com.seeho.tilly.feature.mypage

import android.content.res.Configuration
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

    MyPageContent(
        uiState = uiState,
        onNotificationSettingsChange = viewModel::updateNotificationSettings,
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
