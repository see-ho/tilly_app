package com.seeho.tilly.feature.statistics.component.emotiondistribution

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLabelComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.seeho.tilly.feature.statistics.EmotionDistributionItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 감정 분포 차트 (막대 차트)
 * 각 감정별로 다른 색상의 막대 표시
 */
@Composable
fun EmotionDistributionChart(
    data: List<EmotionDistributionItem>,
    modifier: Modifier = Modifier,
) {
    if (data.isEmpty()) return

    val modelProducer = remember { CartesianChartModelProducer() }

    // 각 감정을 별도 시리즈로 추가하여 개별 색상 적용
    LaunchedEffect(data) {
        withContext(Dispatchers.Default) {
            modelProducer.runTransaction {
                columnSeries {
                    data.forEachIndexed { targetIndex, item ->
                        // 해당 감정 위치에만 값을 넣고, 나머지는 0으로 채움
                        series(
                            List(data.size) { i ->
                                if (i == targetIndex) item.count else 0f
                            }
                        )
                    }
                }
            }
        }
    }

    // X축 라벨: 감정 이름
    val bottomAxisValueFormatter = CartesianValueFormatter { _, x, _ ->
        data.getOrNull(x.toInt())?.label ?: " "
    }

    val columnComponents = data.map { item ->
        rememberLineComponent(
            fill = fill(item.color),
            thickness = 24.dp,
        )
    }

    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberColumnCartesianLayer(
                columnProvider = ColumnCartesianLayer.ColumnProvider.series(columnComponents),
                // Stacked 모드로 같은 x 시리즈를 겹쳐 표시
                mergeMode = { ColumnCartesianLayer.MergeMode.Stacked },
            ),
            startAxis = VerticalAxis.rememberStart(
                // Y축을 정수 단위(1, 2, 3...)로 표시
                itemPlacer = VerticalAxis.ItemPlacer.step(step = { 1.0 }),
                label = rememberAxisLabelComponent(
                    color = MaterialTheme.colorScheme.onSurface,
                    textSize = 10.sp,
                ),
                valueFormatter = { _, y, _ -> y.toInt().toString() }
            ),
            bottomAxis = HorizontalAxis.rememberBottom(
                valueFormatter = bottomAxisValueFormatter,
                itemPlacer = HorizontalAxis.ItemPlacer.aligned(),
                label = rememberAxisLabelComponent(
                    color = MaterialTheme.colorScheme.onSurface,
                    textSize = 11.sp,
                ),
            ),
        ),
        modelProducer = modelProducer,
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp),
        zoomState = rememberVicoZoomState(zoomEnabled = false),
    )
}
