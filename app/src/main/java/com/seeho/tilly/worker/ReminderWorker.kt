package com.seeho.tilly.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.seeho.tilly.core.common.notification.NotificationHelper
import com.seeho.tilly.core.common.notification.NotificationScheduler
import com.seeho.tilly.core.database.dao.TilDao
import com.seeho.tilly.core.datastore.NotificationPreferences
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.Calendar

/**
 * 리마인더 알림 Worker
 * 매일 설정된 시간에 실행되어 TIL 작성 리마인더를 표시
 * 오늘 TIL을 이미 작성했으면 알림을 보내지 않음
 * 실행 완료 후 다음 날 같은 시간에 자동 재스케줄링
 */
@HiltWorker
class ReminderWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val tilDao: TilDao,
    private val notificationHelper: NotificationHelper,
    private val notificationScheduler: NotificationScheduler,
    private val notificationPreferences: NotificationPreferences,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        // 오늘 0시 기준 timestamp 계산
        val todayStart = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        // 오늘 TIL을 이미 작성했으면 알림 건너뛰기
        val todayCount = tilDao.getTodayTilCount(todayStart)
        if (todayCount == 0) {
            // 최근 TIL의 감정에 따라 격려 멘트 결정
            val latestTil = tilDao.getLatestTil()
            val encouragement = getEncouragementByEmotion(latestTil?.emotion)
            notificationHelper.showReminderNotification(encouragement)
        }

        // 다음 날 같은 시간에 재스케줄링 (설정이 여전히 활성화 상태일 때만)
        val settings = notificationPreferences.getSettings()
        if (settings.reminderEnabled) {
            notificationScheduler.scheduleReminder(settings.reminderHour, settings.reminderMinute)
        }

        return Result.success()
    }

    /**
     * 최근 감정에 따른 격려 멘트 생성
     */
    private fun getEncouragementByEmotion(emotion: String?): String {
        return when (emotion) {
            "성취감" -> "어제의 성취감을 이어가볼까요? 🔥"
            "만족" -> "꾸준히 잘하고 있어요! 오늘도 파이팅 💪"
            "평범" -> "작은 기록이 모여 큰 성장이 됩니다 ✨"
            "어려움" -> "어려운 건 성장하고 있다는 증거예요! 응원해요 💙"
            "좌절" -> "괜찮아요, 한 줄이라도 적어보는 게 중요해요 🤗"
            else -> "오늘 하루 배운 것을 기록해보세요! 📝"
        }
    }

    companion object {
        const val WORK_NAME = "tilly_reminder_work"
    }
}
