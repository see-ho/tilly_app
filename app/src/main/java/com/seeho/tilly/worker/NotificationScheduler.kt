package com.seeho.tilly.worker

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.seeho.tilly.core.common.notification.NotificationScheduler
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * NotificationScheduler 구현체
 */
@Singleton
class NotificationSchedulerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : NotificationScheduler {

    private val workManager: WorkManager = WorkManager.getInstance(context)

    override fun scheduleReminder(hour: Int, minute: Int) {
        val delay = calculateDelay(hour, minute)

        val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()

        workManager.enqueueUniqueWork(
            ReminderWorker.WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            workRequest,
        )
    }

    override fun cancelReminder() {
        workManager.cancelUniqueWork(ReminderWorker.WORK_NAME)
    }

    override fun schedulePlan(hour: Int, minute: Int) {
        val delay = calculateDelay(hour, minute)

        val workRequest = OneTimeWorkRequestBuilder<PlanWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()

        workManager.enqueueUniqueWork(
            PlanWorker.WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            workRequest,
        )
    }

    override fun cancelPlan() {
        workManager.cancelUniqueWork(PlanWorker.WORK_NAME)
    }

    private fun calculateDelay(targetHour: Int, targetMinute: Int): Long {
        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, targetHour)
            set(Calendar.MINUTE, targetMinute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        if (target.before(now)) {
            target.add(Calendar.DAY_OF_YEAR, 1)
        }

        return target.timeInMillis - now.timeInMillis
    }
}
