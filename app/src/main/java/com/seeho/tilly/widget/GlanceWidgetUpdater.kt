package com.seeho.tilly.widget

import android.content.Context
import androidx.glance.appwidget.updateAll
import com.seeho.tilly.core.common.widget.WidgetUpdater
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Glance 위젯 갱신 구현체
 *
 * TIL 저장/수정 시 호출되어 스트릭 위젯과 주간 체크 위젯을 즉시 갱신.
 */
@Singleton
class GlanceWidgetUpdater @Inject constructor(
    @ApplicationContext private val context: Context,
) : WidgetUpdater {

    override suspend fun updateAll() {
        StreakWidget().updateAll(context)
        WeeklyCheckWidget().updateAll(context)
    }
}
