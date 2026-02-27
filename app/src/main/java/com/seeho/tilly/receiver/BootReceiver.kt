package com.seeho.tilly.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.seeho.tilly.core.common.notification.NotificationScheduler
import com.seeho.tilly.core.datastore.NotificationPreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * 디바이스 재부팅 시 알림 스케줄을 복원하는 BroadcastReceiver
 */
@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationScheduler: NotificationScheduler

    @Inject
    lateinit var notificationPreferences: NotificationPreferences

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        val settings = notificationPreferences.getSettings()

        // 리마인더가 활성화되어 있으면 다시 스케줄 등록
        if (settings.reminderEnabled) {
            notificationScheduler.scheduleReminder(
                hour = settings.reminderHour,
                minute = settings.reminderMinute,
            )
        }

        // 계획 알람이 활성화되어 있으면 다시 스케줄 등록
        if (settings.planEnabled) {
            notificationScheduler.schedulePlan(
                hour = settings.planHour,
                minute = settings.planMinute,
            )
        }
    }
}
