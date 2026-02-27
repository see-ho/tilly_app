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

/**
 * 계획 알림 Worker
 * 매일 설정된 시간에 실행되어 전날 작성한 '내일 할 일'을 알림으로 표시
 * 실행 완료 후 다음 날 같은 시간에 자동 재스케줄링
 */
@HiltWorker
class PlanWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val tilDao: TilDao,
    private val notificationHelper: NotificationHelper,
    private val notificationScheduler: NotificationScheduler,
    private val notificationPreferences: NotificationPreferences,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        // 가장 최근 TIL에서 '내일 할 일' 조회
        val latestTil = tilDao.getLatestTil()
        val planText = latestTil?.tomorrow

        // '내일 할 일'이 있을 때만 알림 표시
        if (!planText.isNullOrBlank()) {
            notificationHelper.showPlanNotification(planText)
        }

        // 다음 날 같은 시간에 재스케줄링 (설정이 여전히 활성화 상태일 때만)
        val settings = notificationPreferences.getSettings()
        if (settings.planEnabled) {
            notificationScheduler.schedulePlan(settings.planHour, settings.planMinute)
        }

        return Result.success()
    }

    companion object {
        const val WORK_NAME = "tilly_plan_work"
    }
}
