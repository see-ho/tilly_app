package com.seeho.tilly.core.datastore

import android.content.Context
import com.seeho.tilly.core.model.NotificationSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 알림 설정 로컬 저장소
 * SharedPreferences 기반으로 알림 ON/OFF + 시간 설정을 저장/조회
 */
@Singleton
class NotificationPreferences @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // 변경 사항을 Flow로 방출하기 위한 StateFlow
    private val _settingsFlow = MutableStateFlow(loadSettings())
    val settingsFlow: Flow<NotificationSettings> = _settingsFlow.asStateFlow()

    /** 현재 설정값 즉시 반환 */
    fun getSettings(): NotificationSettings = loadSettings()

    /** 설정값 저장 */
    fun updateSettings(settings: NotificationSettings) {
        prefs.edit().apply {
            // 리마인더 알림
            putBoolean(KEY_REMINDER_ENABLED, settings.reminderEnabled)
            putInt(KEY_REMINDER_HOUR, settings.reminderHour)
            putInt(KEY_REMINDER_MINUTE, settings.reminderMinute)
            // 계획 알람
            putBoolean(KEY_PLAN_ENABLED, settings.planEnabled)
            putInt(KEY_PLAN_HOUR, settings.planHour)
            putInt(KEY_PLAN_MINUTE, settings.planMinute)

            apply()
        }
        // StateFlow 갱신
        _settingsFlow.value = settings
    }

    /** SharedPreferences에서 설정값 읽기 */
    private fun loadSettings(): NotificationSettings {
        return NotificationSettings(
            reminderEnabled = prefs.getBoolean(KEY_REMINDER_ENABLED, false),
            reminderHour = prefs.getInt(KEY_REMINDER_HOUR, 20),
            reminderMinute = prefs.getInt(KEY_REMINDER_MINUTE, 0),
            planEnabled = prefs.getBoolean(KEY_PLAN_ENABLED, false),
            planHour = prefs.getInt(KEY_PLAN_HOUR, 9),
            planMinute = prefs.getInt(KEY_PLAN_MINUTE, 0),
        )
    }

    companion object {
        private const val PREFS_NAME = "tilly_notification_prefs"
        private const val KEY_REMINDER_ENABLED = "reminder_enabled"
        private const val KEY_REMINDER_HOUR = "reminder_hour"
        private const val KEY_REMINDER_MINUTE = "reminder_minute"
        private const val KEY_PLAN_ENABLED = "plan_enabled"
        private const val KEY_PLAN_HOUR = "plan_hour"
        private const val KEY_PLAN_MINUTE = "plan_minute"
    }
}
