package com.seeho.tilly.core.common.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 알림 채널 생성 및 알림 표시를 담당하는 헬퍼 클래스
 */
@Singleton
class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val tillyIconRes: Int by lazy {
        context.resources.getIdentifier("ic_tilly", "drawable", context.packageName)
            .takeIf { it != 0 } ?: context.applicationInfo.icon
    }

    companion object {
        // 알림 채널 ID
        const val CHANNEL_REMINDER = "tilly_reminder"
        const val CHANNEL_PLAN = "tilly_plan"

        // 알림 ID
        const val NOTIFICATION_ID_REMINDER = 1001
        const val NOTIFICATION_ID_PLAN = 1002
    }

    /**
     * 앱 시작 시 NotificationChannel 생성
     */
    fun createNotificationChannels() {
        val reminderChannel = NotificationChannel(
            CHANNEL_REMINDER,
            "TIL 리마인더",
            NotificationManager.IMPORTANCE_DEFAULT,
        ).apply {
            description = "매일 TIL 작성을 알려주는 리마인더 알림"
        }

        val planChannel = NotificationChannel(
            CHANNEL_PLAN,
            "계획 알람",
            NotificationManager.IMPORTANCE_DEFAULT,
        ).apply {
            description = "전날 작성한 내일의 계획을 알려주는 알림"
        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(reminderChannel)
        notificationManager.createNotificationChannel(planChannel)
    }

    private fun getTillyBitmap(): android.graphics.Bitmap? = try {
        if (tillyIconRes != 0) {
            val drawable = ContextCompat.getDrawable(context, tillyIconRes)
            drawable?.let {
                val bitmap = android.graphics.Bitmap.createBitmap(
                    it.intrinsicWidth.coerceAtLeast(1),
                    it.intrinsicHeight.coerceAtLeast(1),
                    android.graphics.Bitmap.Config.ARGB_8888,
                )
                val canvas = android.graphics.Canvas(bitmap)
                it.setBounds(0, 0, canvas.width, canvas.height)
                it.draw(canvas)
                bitmap
            }
        } else null
    } catch (_: Exception) {
        null
    }

    /**
     * 리마인더 알림 표시
     */
    fun showReminderNotification(encouragement: String? = null) {
        val contentText = encouragement ?: "오늘 하루 배운 것을 기록해보세요!"

        val builder = NotificationCompat.Builder(context, CHANNEL_REMINDER)
            .setSmallIcon(tillyIconRes)
            .setContentTitle("📚 오늘 TIL 작성하셨나요?")
            .setContentText(contentText)
            .setColor(0xFF00D09C.toInt())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        getTillyBitmap()?.let { builder.setLargeIcon(it) }

        try {
            NotificationManagerCompat.from(context)
                .notify(NOTIFICATION_ID_REMINDER, builder.build())
        } catch (_: SecurityException) {
            // POST_NOTIFICATIONS 권한 미부여 시 무시
        }
    }

    /**
     * 계획 알림 표시
     */
    fun showPlanNotification(planText: String) {

        val builder = NotificationCompat.Builder(context, CHANNEL_PLAN)
            .setSmallIcon(tillyIconRes)
            .setContentTitle("📋 오늘의 학습 계획")
            .setContentText(planText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(planText))
            .setColor(0xFF00D09C.toInt())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        getTillyBitmap()?.let { builder.setLargeIcon(it) }

        try {
            NotificationManagerCompat.from(context)
                .notify(NOTIFICATION_ID_PLAN, builder.build())
        } catch (_: SecurityException) {
            // POST_NOTIFICATIONS 권한 미부여 시 무시
        }
    }
}
