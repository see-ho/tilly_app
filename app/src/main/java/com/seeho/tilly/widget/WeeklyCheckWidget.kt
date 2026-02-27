package com.seeho.tilly.widget

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.seeho.tilly.MainActivity
import com.seeho.tilly.core.database.dao.TilDao
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.firstOrNull
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

// 위젯 색상 상수
private val WidgetBackground = Color(0xFF1C1B1F)
private val CheckedColor = Color(0xFF00E676)
private val UncheckedColor = Color(0xFF2C2C2E)
private val LabelColor = Color(0xFFB0BEC5)

/**
 * 주간 체크 위젯
 * 이번 주 월~일 TIL 작성 여부를 동그라미로 표시하는 Glance 위젯.
 * 완료한 요일은 민트색 ✓, 미완료는 어두운 빈 원으로 표시.
 */
class WeeklyCheckWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val weeklyCheck = getWeeklyCheck(context)

        provideContent {
            GlanceTheme {
                Box(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .cornerRadius(16.dp)
                        .background(ColorProvider(WidgetBackground))
                        .clickable(actionStartActivity<MainActivity>())
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    // 요일별 체크 동그라미 (월~일)
                    Row(
                        modifier = GlanceModifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        val dayLabels = listOf("월", "화", "수", "목", "금", "토", "일")
                        dayLabels.forEachIndexed { index, label ->
                            val isChecked = weeklyCheck.getOrElse(index) { false }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = GlanceModifier.defaultWeight(),
                            ) {
                                // 동그라미 (체크/미체크)
                                Box(
                                    modifier = GlanceModifier
                                        .size(34.dp)
                                        .cornerRadius(17.dp)
                                        .background(
                                            ColorProvider(
                                                if (isChecked) CheckedColor else UncheckedColor,
                                            ),
                                        ),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    if (isChecked) {
                                        Text(
                                            text = "✔",
                                            style = TextStyle(
                                                color = ColorProvider(Color.White),
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold,
                                            ),
                                        )
                                    }
                                }

                                Spacer(modifier = GlanceModifier.height(2.dp))

                                // 요일 라벨
                                Text(
                                    text = label,
                                    style = TextStyle(
                                        color = ColorProvider(
                                            if (isChecked) CheckedColor else LabelColor,
                                        ),
                                        fontSize = 11.sp,
                                    ),
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface WidgetEntryPoint {
        fun tilDao(): TilDao
    }

    /**
     * 이번 주 월~일 TIL 작성 여부 조회
     */
    private suspend fun getWeeklyCheck(context: Context): List<Boolean> {
        return try {
            val entryPoint = EntryPointAccessors.fromApplication(
                context.applicationContext,
                WidgetEntryPoint::class.java,
            )
            val tilDao = entryPoint.tilDao()

            val today = LocalDate.now()
            val zone = ZoneId.systemDefault()
            val monday = today.with(DayOfWeek.MONDAY)
            val sunday = monday.plusDays(6)

            // 월~일 날짜 목록
            val weekDates = (0L..6L).map { monday.plusDays(it) }

            // epoch millis 범위 계산
            val startMillis = monday.atStartOfDay(zone).toInstant().toEpochMilli()
            val endMillis = sunday.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli() - 1

            val tils = tilDao.getTilsBetween(startMillis, endMillis).firstOrNull() ?: emptyList()

            // TIL의 createdAt → LocalDate 변환 후 각 요일 체크
            val tilDates = tils.map { til ->
                Instant.ofEpochMilli(til.createdAt)
                    .atZone(zone)
                    .toLocalDate()
            }.toSet()

            weekDates.map { date -> date in tilDates }
        } catch (e: Exception) {
            List(7) { false }
        }
    }

}
