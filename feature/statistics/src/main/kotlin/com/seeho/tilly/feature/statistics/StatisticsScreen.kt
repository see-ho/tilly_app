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
import com.seeho.tilly.feature.statistics.component.retrospective.MonthlyRetrospective

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
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
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
                    EmotionTrendChart(
                        data = uiState.emotionTrendData,
                        daysInMonth = uiState.daysInMonth,
                    )
                }

                // 2. 학습 키워드 도넛 차트
                StatisticsCard(title = "학습 키워드 분포") {
                    LearningKeywordChart(data = uiState.learningKeywords)
                }

                // 3. 감정 바 그래프
                StatisticsCard(title = "감정 그래프") {
                    EmotionDistributionChart(data = uiState.emotionDistribution)
                }

                // 4. 월간 회고
                MonthlyRetrospective(
                    month = uiState.currentMonth,
                    year = uiState.currentYear,
                    isLoading = uiState.isRetrospectiveLoading,
                    hasRetrospective = uiState.hasRetrospective,
                    onGenerateClick = onGenerateRetrospective,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
    }
}

