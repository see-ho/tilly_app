package com.seeho.tilly.feature.statistics.component.retrospective

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seeho.tilly.core.designsystem.R
import com.seeho.tilly.core.designsystem.component.TillyAlertDialog
import com.seeho.tilly.core.designsystem.component.TillyLoadingIndicator
import com.seeho.tilly.core.designsystem.theme.JetBrainsMonoFontFamily
import com.seeho.tilly.core.designsystem.theme.TillyShapes
import com.seeho.tilly.core.designsystem.util.iconRes
import com.seeho.tilly.core.model.MonthlyRetrospective
import com.seeho.tilly.feature.statistics.DifficultyDistributionItem

@Composable
fun MonthlyRetrospectiveSection(
    month: Int,
    year: Int,
    tilCount: Int,
    averageEmotionScore: Float,
    retrospective: MonthlyRetrospective?,
    difficultyDistribution: List<DifficultyDistributionItem>,
    isLoading: Boolean,
    error: String?,
    onGenerateClick: () -> Unit,
    modifier: Modifier = Modifier,
    minTilCount: Int = 5, // 회고 생성 최소 TIL 개수
) {
    val canGenerate = tilCount >= minTilCount
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(TillyShapes.large)
            .background(MaterialTheme.colorScheme.surface)
            .padding(20.dp),
    ) {
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

        when {
            // 로딩 중
            isLoading -> {
                TillyLoadingIndicator(
                    text = "틸리가 회고를 작성하고 있어요...",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 36.dp),
                )
            }
            // 회고 있음 -> 보고서 표시
            retrospective != null -> {
                RetrospectiveReportCard(
                    month = month,
                    year = year,
                    tilCount = tilCount,
                    averageEmotionScore = averageEmotionScore,
                    retrospective = retrospective,
                    difficultyDistribution = difficultyDistribution,
                )
                Spacer(modifier = Modifier.height(12.dp))
                // 다시 생성 버튼 + 확인 다이얼로그
                var showRegenerateDialog by remember { mutableStateOf(false) }

                if (showRegenerateDialog) {
                    TillyAlertDialog(
                        onDismissRequest = { showRegenerateDialog = false },
                        onConfirm = {
                            showRegenerateDialog = false
                            onGenerateClick()
                        },
                        title = "회고 다시 생성",
                        text = "회고를 다시 생성하면 50코인이 소모돼요.\n기존 회고는 새로운 회고로 대체됩니다.",
                        confirmText = "생성하기",
                        dismissText = "취소",
                        confirmButtonColor = MaterialTheme.colorScheme.primary,
                    )
                }

                Button(
                    onClick = { showRegenerateDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "다시 생성하기",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                        ),
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Image(
                        painter = painterResource(id = R.drawable.ic_coin),
                        contentDescription = "코인",
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(modifier = Modifier.size(2.dp))
                    Text(
                        text = "50",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                        ),
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
            // 회고 없음 → 생성 버튼 (최소 TIL 수 체크)
            else -> {
                // 에러 메시지
                if (error != null) {
                    Text(
                        text = error,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 8.dp),
                    )
                }
                // 최소 TIL 수 미달 안내
                if (!canGenerate) {
                    Text(
                        text = "TIL ${minTilCount}개 이상 작성하면 회고를 생성할 수 있어요 (현재 ${tilCount}개)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 8.dp),
                    )
                }
                Button(
                    onClick = onGenerateClick,
                    enabled = canGenerate,
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
            }
        }
    }
}

// ===== 대시 보더 Modifier =====

/** 대시 라인 보더를 그리는 Modifier */
private fun Modifier.dashedBorder(
    color: Color,
    cornerRadius: Float = 24f,
    dashLength: Float = 8f,
    gapLength: Float = 6f,
    strokeWidth: Float = 2f,
) = this.drawBehind {
    drawRoundRect(
        color = color,
        cornerRadius = CornerRadius(cornerRadius, cornerRadius),
        style = Stroke(
            width = strokeWidth,
            pathEffect = PathEffect.dashPathEffect(
                floatArrayOf(dashLength, gapLength), 0f
            )
        )
    )
}

// ===== 메인 리포트 카드 =====

@Composable
private fun RetrospectiveReportCard(
    month: Int,
    year: Int,
    tilCount: Int,
    averageEmotionScore: Float,
    retrospective: MonthlyRetrospective,
    difficultyDistribution: List<DifficultyDistributionItem>,
    modifier: Modifier = Modifier,
) {
    val borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.3f),
        ),
        border = BorderStroke(width = 1.dp, color = borderColor),
        shape = MaterialTheme.shapes.large,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // 제목
            Text(
                text = "📋 ${year}년 ${month}월 학습 회고",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 18.sp,
                    lineHeight = 28.sp,
                ),
                color = MaterialTheme.colorScheme.primary,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 한 줄 요약
            DashedSection(borderColor = borderColor) {
                SectionTitle(text = "한 줄 요약")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "• 총 ${tilCount}일 기록",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = JetBrainsMonoFontFamily,
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "• ${retrospective.summary}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = JetBrainsMonoFontFamily,
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 난이도 분포
            DashedSection(borderColor = borderColor) {
                SectionTitle(text = "🐾 난이도 분포")
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    difficultyDistribution.forEach { item ->
                        DifficultyDotItem(
                            label = item.difficulty.displayName,
                            count = item.count,
                            iconRes = item.difficulty.iconRes,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 성장 포인트
            if (retrospective.growthPoints.isNotEmpty()) {
                DashedSection(borderColor = borderColor) {
                    SectionTitle(text = "🐾 성장 포인트")
                    Spacer(modifier = Modifier.height(8.dp))
                    retrospective.growthPoints.forEach { point ->
                        Text(
                            text = "• $point",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = JetBrainsMonoFontFamily,
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(start = 8.dp, bottom = 4.dp),
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // 개선 포인트
            if (retrospective.improvementPoints.isNotEmpty()) {
                DashedSection(borderColor = borderColor) {
                    SectionTitle(text = "🐾 개선 포인트")
                    Spacer(modifier = Modifier.height(8.dp))
                    retrospective.improvementPoints.forEach { point ->
                        Text(
                            text = "❗ $point",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = JetBrainsMonoFontFamily,
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(start = 8.dp, bottom = 4.dp),
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // 다음 달 추천
            if (retrospective.nextMonthSuggestions.isNotEmpty()) {
                DashedSection(borderColor = borderColor) {
                    SectionTitle(text = "🐾 다음 달 추천")
                    Spacer(modifier = Modifier.height(8.dp))
                    retrospective.nextMonthSuggestions.forEach { suggestion ->
                        Text(
                            text = "• $suggestion",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = JetBrainsMonoFontFamily,
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(start = 8.dp, bottom = 4.dp),
                        )
                    }
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // 틸리의 한마디 (별도 카드)
    TillyCommentCard(
        averageEmotionScore = averageEmotionScore,
        borderColor = borderColor,
    )
}

// ===== 재사용 컴포넌트 =====

/** 대시 보더가 있는 섹션 컨테이너 */
@Composable
private fun DashedSection(
    borderColor: Color,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .dashedBorder(color = borderColor, cornerRadius = 16f)
            .padding(12.dp),
    ) {
        Column { content() }
    }
}

/** 섹션 타이틀 (발바닥 이모지 포함) */
@Composable
private fun SectionTitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier,
    )
}

/** 난이도 도트 아이템 (PNG 아이콘 + 라벨 + 개수, 세로 배치) */
@Composable
private fun DifficultyDotItem(
    label: String,
    count: Int,
    iconRes: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        // 난이도 도트 아이콘
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            modifier = Modifier.size(20.dp),
        )
        // 라벨
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        // 개수
        Text(
            text = "${count}개",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold,
                fontFamily = JetBrainsMonoFontFamily,
            ),
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}


/** 틸리의 한마디 카드 (하단 별도 카드) */
@Composable
private fun TillyCommentCard(
    averageEmotionScore: Float,
    borderColor: Color,
    modifier: Modifier = Modifier,
) {

    // 평균 감정 점수에 따른 멘트
    val tillyMessage = when {
        averageEmotionScore >= 4f ->
            "이번 달 정말 수고했어요.\n평균 감정 점수는 ${String.format(java.util.Locale.KOREA, "%.1f", averageEmotionScore)}점!\n이 기세로 다음 달도 화이팅! 🔥"
        averageEmotionScore >= 3f ->
            "이번 달 정말 수고했어요.\n평균 감정 점수는 ${String.format(java.util.Locale.KOREA, "%.1f", averageEmotionScore)}점!\n다음 달엔 조금 더 도전해볼까요?"
        averageEmotionScore > 0f ->
            "이번 달 고생 많았어요.\n평균 감정 점수는 ${String.format(java.util.Locale.KOREA, "%.1f", averageEmotionScore)}점!\n힘들었지만 성장하고 있어요 💪"
        else ->
            "이번 달도 함께해줘서 고마워요!\n다음 달도 틸리가 응원할게요 🐾"
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.3f),
        ),
        shape = MaterialTheme.shapes.large,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .dashedBorder(color = borderColor, cornerRadius = 16f)
                .padding(16.dp),
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                SectionTitle(text = "🐾 틸리의 한마디")

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    // 회고 전용 틸리 이미지
                    Image(
                        painter = painterResource(id = R.drawable.ic_retrospective),
                        contentDescription = null,
                        modifier = Modifier.size(56.dp),
                        contentScale = ContentScale.Fit,
                    )
                    // 멘트
                    Text(
                        text = tillyMessage,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = JetBrainsMonoFontFamily,
                            lineHeight = 22.sp,
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }
}
