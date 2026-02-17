package com.seeho.tilly.feature.statistics.component.learningkeyword

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import com.seeho.tilly.feature.statistics.LearningKeywordItem
import kotlin.math.cos
import kotlin.math.sin

/**
 * 학습 키워드 분포 도넛 차트
 * - Canvas로 도넛 차트 그리기
 * - 지시선(Guidelines) 및 퍼센트 텍스트 표시
 * - 하단 범례 포함
 */
@Composable
fun LearningKeywordChart(
    data: List<LearningKeywordItem>,
    modifier: Modifier = Modifier,
) {
    if (data.isEmpty()) return

    val total = remember(data) { data.sumOf { it.percent } }
    if (total == 0) return
    val textMeasurer = rememberTextMeasurer()
    val subtextColor = MaterialTheme.colorScheme.onSurfaceVariant
    val textStyle = MaterialTheme.typography.bodySmall.copy(
        fontWeight = FontWeight.Medium,
        color = subtextColor
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(300.dp) // 차트 영역 크기
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxWidth().height(200.dp)) {
                val strokeWidth = 30.dp.toPx()
                val radius = (size.minDimension - strokeWidth) / 2 * 0.6f // 도넛 크기 조절
                val center = Offset(size.width / 2, size.height / 2)
                var startAngle = -90f // 12시 방향 시작

                data.forEach { item ->
                    val sweepAngle = (item.percent / total.toFloat()) * 360f
                    val color = item.color

                    // 도넛 조각 그리기
                    drawArc(
                        color = color,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        topLeft = Offset(center.x - radius, center.y - radius),
                        size = Size(radius * 2, radius * 2),
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                    )

                    // 지시선 및 텍스트 그리기 (중간 각도)
                    val midAngle = startAngle + sweepAngle / 2
                    val midRad = Math.toRadians(midAngle.toDouble())

                    // 지시선 시작점 (도넛 바깥쪽)
                    val lineStartRadius = radius + strokeWidth / 2 + 5.dp.toPx()
                    val startX = center.x + lineStartRadius * cos(midRad).toFloat()
                    val startY = center.y + lineStartRadius * sin(midRad).toFloat()

                    // 지시선 끝점 (더 바깥쪽)
                    val lineEndRadius = lineStartRadius + 15.dp.toPx()
                    val endX = center.x + lineEndRadius * cos(midRad).toFloat()
                    val endY = center.y + lineEndRadius * sin(midRad).toFloat()

                    // 지시선 그리기
                    drawLine(
                        color = subtextColor,
                        start = Offset(startX, startY),
                        end = Offset(endX, endY),
                        strokeWidth = 1.dp.toPx()
                    )

                    // 텍스트 위치 계산 (지시선 끝점 근처)
                    val text = "${item.percent}%"
                    val textLayoutResult = textMeasurer.measure(text, textStyle)
                    val textWidth = textLayoutResult.size.width
                    val textHeight = textLayoutResult.size.height

                    // 텍스트 위치 조정 (좌/우/상/하)
                    var textX = endX
                    var textY = endY - textHeight / 2

                    if (midAngle > -90 && midAngle < 90) { // 우측 (12시 ~ 6시)
                        textX += 5.dp.toPx()
                    } else if (midAngle >= 90 && midAngle < 270) { // 좌측 (6시 ~ 12시)
                        textX -= (textWidth + 5.dp.toPx())
                    } else { // 270~ (상단 좌측)
                         textX -= (textWidth + 5.dp.toPx())
                    }

                    drawText(
                        textLayoutResult = textLayoutResult,
                        topLeft = Offset(textX, textY)
                    )

                    startAngle += sweepAngle
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            data.forEach { item ->
                LearningKeywordLegendItem(
                    label = item.keyword,
                    color = item.color,
                    modifier = Modifier.padding(horizontal = 8.dp),
                )
            }
        }
    }
}

@Composable
private fun LearningKeywordLegendItem(
    label: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Canvas(modifier = Modifier.size(8.dp)) {
            drawCircle(color = color)
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}
