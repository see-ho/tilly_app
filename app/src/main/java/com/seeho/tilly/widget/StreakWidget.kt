package com.seeho.tilly.widget

import android.content.Context
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.clickable
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import androidx.glance.appwidget.cornerRadius
import com.seeho.tilly.MainActivity
import com.seeho.tilly.core.database.dao.CoinDao
import com.seeho.tilly.core.designsystem.R as DesignR
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.firstOrNull

// 위젯 색상 상수 (Compose Color 사용 — Glance API 요구)
private val WidgetBackground = Color(0xFF1C1B1F)
private val AccentMint = Color(0xFF00D09C)
private val SubtleGray = Color(0xFFB0BEC5)

/**
 *  연속 작성 스트릭 위젯
 */
class StreakWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        // DB에서 직접 streakCount 조회
        val streakCount = getStreakCount(context)
        // 스트릭에 따른 동기부여 메시지 생성
        val motivationMessage = getMotivationMessage(streakCount)

        provideContent {
            GlanceTheme {
                // 위젯 전체 영역: 탭하면 앱 실행
                Box(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .cornerRadius(16.dp)
                        .background(ColorProvider(WidgetBackground))
                        .clickable(actionStartActivity<MainActivity>())
                        .padding(12.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = GlanceModifier.fillMaxSize(),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Image(
                                provider = ImageProvider(DesignR.drawable.ic_fire),
                                contentDescription = "불꽃",
                                modifier = GlanceModifier.size(36.dp),
                            )
                            Spacer(modifier = GlanceModifier.width(4.dp))
                            Text(
                                text = "$streakCount",
                                style = TextStyle(
                                    color = ColorProvider(AccentMint),
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                ),
                            )
                        }

                        Spacer(modifier = GlanceModifier.height(2.dp))

                        // 동기부여 메시지
                        Text(
                            text = motivationMessage,
                            style = TextStyle(
                                color = ColorProvider(SubtleGray),
                                fontSize = 14.sp,
                            ),
                        )
                    }
                }
            }
        }
    }

    /**
     * Hilt EntryPoint를 통해 CoinDao에 접근하기 위한 인터페이스
     */
    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface WidgetEntryPoint {
        fun coinDao(): CoinDao
    }

    /**
     * Hilt EntryPoint를 통해 싱글턴 CoinDao에서 streakCount 조회
     */
    private suspend fun getStreakCount(context: Context): Int {
        return try {
            val entryPoint = EntryPointAccessors.fromApplication(
                context.applicationContext,
                WidgetEntryPoint::class.java,
            )
            val coinEntity = entryPoint.coinDao().getUserCoin().firstOrNull()
            coinEntity?.streakCount ?: 0
        } catch (e: Exception) {
            Log.w("StreakWidget", "Failed to get streak count", e)
            0
        }
    }

    /**
     * 스트릭 수에 따라 다른 동기부여 메시지 반환
     */
    private fun getMotivationMessage(streak: Int): String {
        return when {
            streak == 0 -> "오늘부터 시작해볼까요?"
            streak < 3 -> "일 연속 작성 중!"
            streak < 7 -> "좋은 습관이 되고 있어요 🌱"
            streak < 14 -> "대단해요! 계속 가봐요 💪"
            streak < 30 -> "멈추지 마세요! 🔥"
            else -> "전설의 학습자! 🏆"
        }
    }
}
