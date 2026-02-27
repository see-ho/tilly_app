package com.seeho.tilly.feature.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.seeho.tilly.core.designsystem.component.TillyLoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seeho.tilly.feature.statistics.component.StatisticsCard
import com.seeho.tilly.feature.statistics.component.emotiondistribution.EmotionDistributionChart
import com.seeho.tilly.feature.statistics.component.emotiontrendchart.EmotionTrendChart
import com.seeho.tilly.feature.statistics.component.learningkeyword.LearningKeywordChart
import com.seeho.tilly.feature.statistics.component.monthselector.MonthSelector
import com.seeho.tilly.feature.statistics.component.retrospective.MonthlyRetrospectiveSection

@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    StatisticsContent(
        uiState = uiState,
        onPreviousMonth = viewModel::onPreviousMonth,
        onNextMonth = viewModel::onNextMonth,
        onGenerateRetrospective = viewModel::onGenerateRetrospective,
        modifier = modifier
    )
}

@Composable
private fun StatisticsContent(
    uiState: StatisticsUiState,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onGenerateRetrospective: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    // 회고가 생성 완료되면 하단으로 자동 스크롤
    LaunchedEffect(uiState.retrospective) {
        if (uiState.retrospective != null) {
            scrollState.animateScrollTo(scrollState.maxValue)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
            // Month Selector
            MonthSelector(
                month = uiState.currentMonth,
                year = uiState.currentYear,
                tilCount = uiState.tilCount,
                canGoNext = uiState.canGoNext,
                onPreviousMonth = onPreviousMonth,
                onNextMonth = onNextMonth,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
            )

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    TillyLoadingIndicator()
                }
            } else if (uiState.tilCount == 0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "// No data found",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "이번 달 작성한 TIL이 없어요",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            } else {
                // 1. 월간 감정 추세 꺾은선 그래프
                StatisticsCard(title = "월간 감정 점수 추세") {
                    if (uiState.emotionTrendData.isEmpty()) {
                        EmptyChartMessage(text = "감정 데이터가 아직 없어요")
                    } else {
                        EmotionTrendChart(
                            data = uiState.emotionTrendData,
                            daysInMonth = uiState.daysInMonth,
                        )
                    }
                }

                // 2. 학습 키워드 도넛 차트
                StatisticsCard(title = "학습 키워드 분포") {
                    if (uiState.learningKeywords.isEmpty()) {
                        EmptyChartMessage(text = "키워드 데이터가 아직 없어요")
                    } else {
                        LearningKeywordChart(data = uiState.learningKeywords)
                    }
                }

                // 3. 감정 바 그래프
                StatisticsCard(title = "감정 그래프") {
                    val hasEmotionData = uiState.emotionDistribution.any { it.count > 0f }
                    if (!hasEmotionData) {
                        EmptyChartMessage(text = "감정 분포 데이터가 아직 없어요")
                    } else {
                        EmotionDistributionChart(data = uiState.emotionDistribution)
                    }
                }

                // 4. 월간 회고
                MonthlyRetrospectiveSection(
                    month = uiState.currentMonth,
                    year = uiState.currentYear,
                    tilCount = uiState.tilCount,
                    averageEmotionScore = uiState.averageEmotionScore,
                    retrospective = uiState.retrospective,
                    difficultyDistribution = uiState.difficultyDistribution,
                    isLoading = uiState.isRetrospectiveLoading,
                    error = uiState.retrospectiveError,
                    onGenerateClick = onGenerateRetrospective,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
    }
}

/** 차트 데이터가 없을 때 표시하는 빈 상태 메시지 */
@Composable
private fun EmptyChartMessage(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "// No data",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
