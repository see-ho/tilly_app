package com.seeho.tilly.feature.statistics.component.retrospective

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seeho.tilly.core.designsystem.theme.JetBrainsMonoFontFamily

/**
 * 월간 회고 섹션 컴포넌트
 */
@Composable
fun MonthlyRetrospective(
    month: Int,
    year: Int,
    hasRetrospective: Boolean,
    isLoading: Boolean,
    onGenerateClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                imageVector = Icons.Outlined.Description,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = "월간 회고",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 18.sp,
                    lineHeight = 28.sp,
                ),
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 회고 생성 버튼
        Button(
            onClick = onGenerateClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ),
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "회고 생성하기",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                ),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 회고 보고서 카드
        if (hasRetrospective) {
            RetrospectiveReportCard(
                month = month,
                year = year,
            )
        }
    }
}

/**
 * 회고 보고서 카드
 * TODO 현재 임시
 */
@Composable
private fun RetrospectiveReportCard(
    month: Int,
    year: Int,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.3f),
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
        ),
        shape = MaterialTheme.shapes.large,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            // 제목
            Text(
                text = "${year}년 ${month}월 학습 회고 ",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 18.sp,
                    lineHeight = 28.sp,
                ),
                color = MaterialTheme.colorScheme.primary,
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 이번 달 요약
            RetrospectiveSection(title = "이번 달 요약")
            Text(
                text = "이번 달에는 총 3번의 학습을 기록했어요. 평균 감정 점수는 7.7/10점이었고, 가장 많이 느낀 감정은 😄 이었습니다.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = JetBrainsMonoFontFamily,
                ),
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 난이도 분포
            RetrospectiveSection(title = "난이도 분포")
            val difficulties = listOf("쉬움: 0일", "보통: 0일", "어려움: 0일")
            difficulties.forEach { item ->
                Text(
                    text = item,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = JetBrainsMonoFontFamily,
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(start = 16.dp),
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 틸리의 분석
            RetrospectiveSection(title = "틸리의 분석")
            Text(
                text = "좋은 진전을 보이고 있어요! 꾸준히 배우고 성장하는 모습이 보입니다. 조금 더 도전적인 주제들도 시도해보시면 어떨까요?",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = JetBrainsMonoFontFamily,
                ),
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "균형 잡힌 학습 접근 방식을 유지하고 있네요. 좋은 전략입니다!",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = JetBrainsMonoFontFamily,
                ),
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 다음 달 목표
            RetrospectiveSection(title = "다음 달 목표")
            Text(
                text = "지금까지의 진행 상황을 바탕으로 제안드립니다:",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = JetBrainsMonoFontFamily,
                ),
                color = MaterialTheme.colorScheme.onSurface,
            )

            val goals = listOf(
                "난이도를 조금 높여보는 건 어떨까요?",
                "더 도전적인 프로젝트를 시작해보세요",
                "이번 달 가장 흥미로웠던 주제를 더 깊이 탐구해보세요",
            )
            goals.forEach { goal ->
                Text(
                    text = goal,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = JetBrainsMonoFontFamily,
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(start = 16.dp),
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "계속 배우고, 계속 성장해나가세요! 틸리가 응원할게요 ",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = JetBrainsMonoFontFamily,
                ),
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

/**
 * 회고 보고서 내 섹션 제목
 */
@Composable
private fun RetrospectiveSection(
    title: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        Spacer(modifier = Modifier.height(4.dp))
    }
}
