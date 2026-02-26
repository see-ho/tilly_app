package com.seeho.tilly.core.common.widget

interface WidgetUpdater {
    /** 모든 Tilly 위젯을 갱신 */
    suspend fun updateAll()
}
