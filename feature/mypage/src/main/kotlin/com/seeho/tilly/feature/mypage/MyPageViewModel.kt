package com.seeho.tilly.feature.mypage

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seeho.tilly.core.datastore.NotificationPreferences
import com.seeho.tilly.core.domain.GetTotalTilCountUseCase
import com.seeho.tilly.core.domain.GetUserCoinUseCase
import com.seeho.tilly.core.domain.GetWeeklyTilCheckUseCase
import com.seeho.tilly.core.model.NotificationSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getWeeklyTilCheckUseCase: GetWeeklyTilCheckUseCase,
    private val getTotalTilCountUseCase: GetTotalTilCountUseCase,
    private val getUserCoinUseCase: GetUserCoinUseCase,
    private val notificationPreferences: NotificationPreferences,
    application: Application,
) : ViewModel() {

    // 알림 설정 Flow
    private val notificationFlow = notificationPreferences.settingsFlow

    // 앱 버전
    private val appVersion: String = try {
        val packageInfo = application.packageManager.getPackageInfo(application.packageName, 0)
        packageInfo.versionName ?: "1.0.0"
    } catch (_: Exception) {
        "1.0.0"
    }

    /**
     * 마이페이지 UI 상태
     */
    val uiState: StateFlow<MyPageUiState> = combine(
        getWeeklyTilCheckUseCase(),
        getTotalTilCountUseCase(),
        getUserCoinUseCase(),
        notificationFlow,
    ) { weeklyCheck, totalCount, userCoin, notifSettings ->
        MyPageUiState(
            weeklyCheck = weeklyCheck,
            streakCount = userCoin.streakCount,
            totalTilCount = totalCount,
            notificationSettings = notifSettings,
            appVersion = appVersion,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MyPageUiState(appVersion = appVersion),
    )

    /** 알림 설정 업데이트 */
    fun updateNotificationSettings(settings: NotificationSettings) {
        notificationPreferences.updateSettings(settings)
    }
}
