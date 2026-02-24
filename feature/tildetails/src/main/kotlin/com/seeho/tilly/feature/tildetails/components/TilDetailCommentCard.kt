package com.seeho.tilly.feature.tildetails.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seeho.tilly.core.designsystem.R
import com.seeho.tilly.core.designsystem.component.EmotionLabelBadge
import com.seeho.tilly.core.designsystem.component.EmotionScoreIndicator
import com.seeho.tilly.core.designsystem.theme.TillyTheme
import com.seeho.tilly.core.model.Difficulty
import com.seeho.tilly.core.model.Emotion
import com.seeho.tilly.core.model.Til

import com.seeho.tilly.core.designsystem.util.color
import com.seeho.tilly.core.designsystem.util.iconRes

@Composable
fun TilDetailCommentCard(
    til: Til,
    modifier: Modifier = Modifier,
) {
    val feedback = til.feedback ?: "분석된 내용이 없습니다.\n수정모드에서 재분석을 통해\n틸리의 분석을 받아보세요!"
    val emotionScore = til.emotionScore
    val difficultyLevel = til.difficultyLevel
    
    // 분석 여부 판단
    val hasAnalysis = emotionScore != null || til.emotion != null

    // 감정별 틸리 아이콘
    val emotion = til.emotion
    val tillyIcon = when (emotion) {
        Emotion.ACHIEVEMENT -> R.drawable.ic_tilly_accomplished
        Emotion.SATISFACTION -> R.drawable.ic_tilly_satisfied
        Emotion.NORMAL -> R.drawable.ic_tilly_normal
        Emotion.HARD -> R.drawable.ic_tilly_challenged
        Emotion.FRUSTRATION -> R.drawable.ic_tilly_frustrated
        null -> R.drawable.ic_tilly_normal
    }

    val difficultyIcon = difficultyLevel?.iconRes
    val difficultyColor = difficultyLevel?.color


    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        shape = MaterialTheme.shapes.medium,
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "틸리의 한마디",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(4.dp))

            if (!hasAnalysis) {
                // 미분석
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_retrospective),
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(color = MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.small)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                                shape = MaterialTheme.shapes.small
                            )
                            .padding(12.dp)
                    ) {
                        Text(
                            text = feedback,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.4f
                        )
                    }
                }
            } else {
                // 분석 완료
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.small)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(12.dp)
                ) {
                    Text(
                        text = feedback,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.4f
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = tillyIcon),
                        contentDescription = null,
                        modifier = Modifier.size(100.dp),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        if (emotionScore != null) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = "감정",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    EmotionScoreIndicator(
                                        score = emotionScore,
                                        blockWidth = 20.dp,
                                        blockHeight = 12.dp
                                    )
                                    til.emotion?.let { emo ->
                                        EmotionLabelBadge(
                                            label = emo.label,
                                            color = emo.color,
                                        )
                                    }

                                    Spacer(modifier = Modifier.weight(1f))

                                    Text(
                                        text = "${emotionScore}/5",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }

                        if (difficultyLevel != null && difficultyIcon != null && difficultyColor != null) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = "난이도",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Image(
                                        painter = painterResource(id = difficultyIcon),
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = difficultyLevel.displayName,
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = difficultyColor
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TilDetailCommentCardPreview() {
    TillyTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            TilDetailCommentCard(
                til = Til(
                    title = "Test",
                    learned = "Test",
                    emotionScore = 4,
                    difficultyLevel = Difficulty.HARD,
                    feedback = "정말 잘했는데요? 상태 관리를 공부했군요. 난이도가 어려운데 잘 해낸 것 같아요. 앞으로도 화이팅해봐요!",
                    createdAt = 0
                ),
            )
        }
    }
}
