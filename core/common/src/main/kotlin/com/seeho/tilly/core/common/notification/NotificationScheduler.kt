package com.seeho.tilly.core.common.notification

/**
 * 알림 스케줄링 인터페이스
 */
interface NotificationScheduler {
    /** 리마인더 알림 스케줄링 */
    fun scheduleReminder(hour: Int, minute: Int)

    /** 리마인더 알림 취소 */
    fun cancelReminder()

    /** 계획 알림 스케줄링 */
    fun schedulePlan(hour: Int, minute: Int)

    /** 계획 알림 취소 */
    fun cancelPlan()
}
