package com.seeho.tilly.feature.statistics.component.emotiontrendchart

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisGuidelineComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLabelComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.core.cartesian.Zoom
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.marker.CartesianMarkerValueFormatter
import com.patrykandpatrick.vico.core.common.Dimensions
import com.patrykandpatrick.vico.core.common.Fill
import com.patrykandpatrick.vico.core.common.LayeredComponent
import com.patrykandpatrick.vico.core.common.component.ShapeComponent
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import com.seeho.tilly.core.designsystem.theme.NeoTerminalGreen
import com.seeho.tilly.feature.statistics.EmotionTrendItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 감정 추세 라인 차트 컴포넌트
 * - X축: 월의 모든 날짜 표시 (1~28/30/31)
 * - 데이터 있는 날에 점 표시 + 직선 연결
 * - 터치 시 날짜+점수 마커
 */
@Composable
fun EmotionTrendChart(
    data: List<EmotionTrendItem>,
    daysInMonth: Int,
    modifier: Modifier = Modifier,
) {
    if (data.isEmpty()) return

    val modelProducer = remember { CartesianChartModelProducer() }

    // X값은 일자(day-of-month), Y값은 점수
    LaunchedEffect(data) {
        withContext(Dispatchers.Default) {
            modelProducer.runTransaction {
                lineSeries {
                    series(
                        x = data.map { it.day },
                        y = data.map { it.score }
                    )
                }
            }
        }
    }

    // X축 날짜 포맷터 (일자 숫자 표시)
    val bottomAxisValueFormatter = CartesianValueFormatter { _, x, _ ->
        x.toInt().toString()
    }

    // 데이터 포인트에 기본적으로 표시될 점
    val pointComponent = rememberShapeComponent(
        fill = Fill(NeoTerminalGreen.toArgb()),
        shape = CorneredShape.Pill,
    )

    // 마커 라벨 배경
    val labelBackground = rememberShapeComponent(
        fill = Fill(MaterialTheme.colorScheme.background.toArgb()),
        shape = CorneredShape.Pill,
        strokeFill = Fill(MaterialTheme.colorScheme.outline.toArgb()),
        strokeThickness = 1.dp,
    )

    // 마커 라벨 텍스트
    val label = rememberTextComponent(
        color = MaterialTheme.colorScheme.onSurface,
        textSize = 12.sp,
        padding = Dimensions(8f, 4f, 8f, 4f),
        background = labelBackground,
        minWidth = TextComponent.MinWidth.fixed(40f),
    )

    // 마커 인디케이터 내부 원
    val surfaceColor = MaterialTheme.colorScheme.surface.toArgb()

    // 수직 가이드라인
    val guideline = rememberAxisGuidelineComponent()

    // 터치 시 날짜 + 점수 표시
    val primaryColor = MaterialTheme.colorScheme.primary.toArgb()
    val valueFormatter = remember(data, primaryColor) {
        CartesianMarkerValueFormatter { _, targets ->
            val target = targets.firstOrNull() ?: return@CartesianMarkerValueFormatter ""
            val day = target.x.toInt()
            val item = data.find { it.day == day } ?: return@CartesianMarkerValueFormatter ""
            val dayText = "${day}일 · "
            val scoreText = "${item.score.toInt()}점"
            SpannableStringBuilder(dayText + scoreText).apply {
                setSpan(
                    ForegroundColorSpan(primaryColor),
                    dayText.length,
                    length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
                )
            }
        }
    }

    // 터치 시 표시되는 마커
    val marker = rememberDefaultCartesianMarker(
        label = label,
        valueFormatter = valueFormatter,
        indicator = { color ->
            LayeredComponent(
                rear = ShapeComponent(
                    fill = Fill(color.copy(alpha = 0.15f).toArgb()),
                    shape = CorneredShape.Pill,
                ),
                front = LayeredComponent(
                    rear = ShapeComponent(
                        fill = Fill(color.toArgb()),
                        shape = CorneredShape.Pill,
                    ),
                    front = ShapeComponent(
                        fill = Fill(surfaceColor),
                        shape = CorneredShape.Pill,
                    ),
                    padding = Dimensions(5f),
                ),
                padding = Dimensions(10f),
            )
        },
        indicatorSize = 36.dp,
        guideline = guideline,
    )

    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(
                lineProvider = LineCartesianLayer.LineProvider.series(
                    LineCartesianLayer.Line(
                        fill = LineCartesianLayer.LineFill.single(fill(NeoTerminalGreen)),
                        areaFill = null,
                        // 직선 연결
                        pointConnector = LineCartesianLayer.PointConnector.cubic(curvature = 0f),
                        // 데이터 포인트에 점 표시
                        pointProvider = LineCartesianLayer.PointProvider.single(
                            LineCartesianLayer.Point(
                                component = pointComponent,
                                sizeDp = 8f,
                            )
                        ),
                    )
                ),
                // X축: 1~마지막일, Y축: 0~5 고정
                rangeProvider = CartesianLayerRangeProvider.fixed(
                    minX = 1.0,
                    maxX = daysInMonth.toDouble(),
                    minY = 0.0,
                    maxY = 5.0,
                ),
            ),
            startAxis = VerticalAxis.rememberStart(
                itemPlacer = VerticalAxis.ItemPlacer.step(step = { 1.0 }),
                label = rememberAxisLabelComponent(
                    color = MaterialTheme.colorScheme.onSurface,
                    textSize = 10.sp
                ),
                valueFormatter = { _, y, _ -> y.toInt().toString() }
            ),
            bottomAxis = HorizontalAxis.rememberBottom(
                valueFormatter = bottomAxisValueFormatter,
                itemPlacer = HorizontalAxis.ItemPlacer.aligned(
                    spacing = 1,
                    shiftExtremeLines = true,
                    addExtremeLabelPadding = true
                ),
                label = rememberAxisLabelComponent(
                    color = MaterialTheme.colorScheme.onSurface,
                    textSize = 9.sp,
                ),
                guideline = null,
            ),
            marker = marker,
        ),
        modelProducer = modelProducer,
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp),
        zoomState = rememberVicoZoomState(
            zoomEnabled = false,
            initialZoom = Zoom.Content
        ),
        scrollState = rememberVicoScrollState(scrollEnabled = false),
    )
}
