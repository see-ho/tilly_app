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
import com.seeho.tilly.core.designsystem.component.EmotionScoreIndicator
import com.seeho.tilly.core.designsystem.theme.DifficultyEasy
import com.seeho.tilly.core.designsystem.theme.DifficultyHard
import com.seeho.tilly.core.designsystem.theme.DifficultyMedium
import com.seeho.tilly.core.designsystem.theme.DifficultyVeryHard
import com.seeho.tilly.core.designsystem.theme.TillyTheme
import com.seeho.tilly.core.model.Til

@Composable
fun TilDetailCommentCard(
    til: Til,
    modifier: Modifier = Modifier,
) {
    val feedback = til.feedback ?: "분석된 내용이 없습니다."
    val emotionScore = til.emotionScore ?: 0
    val difficultyLevel = til.difficultyLevel ?: "NORMAL"

    //TODO 감정 점수 별 틸리 2개 더(좌절, 보통) 등록
    val tillyIcon = when {
        emotionScore >= 4 -> R.drawable.ic_tilly_satisfied
        emotionScore >= 3 -> R.drawable.ic_tilly_accomplished
        else -> R.drawable.ic_tilly_challenged
    }

    val difficultyIcon = when (difficultyLevel.uppercase()) {
        "EASY" -> R.drawable.ic_difficult_easy
        "NORMAL" -> R.drawable.ic_difficult_normal
        "HARD" -> R.drawable.ic_difficult_hard
        "VERYHARD" -> R.drawable.ic_difficult_veryhard
        else -> R.drawable.ic_difficult_normal
    }

    val difficultyColor = when (difficultyLevel.uppercase()) {
        "EASY" -> DifficultyEasy
        "NORMAL" -> DifficultyMedium
        "HARD" -> DifficultyHard
        "VERYHARD" ->DifficultyVeryHard
        else -> DifficultyMedium
    }


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
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Emotion",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            EmotionScoreIndicator(
                                score = emotionScore * 2,
                                blockWidth = 20.dp,
                                blockHeight = 12.dp
                            )
                            
                            Spacer(modifier = Modifier.weight(1f))
                            
                            Text(
                                text = "${emotionScore * 2}/10",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Difficulty",
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
                                text = difficultyLevel.uppercase(),
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
                    difficultyLevel = "HARD",
                    feedback = "정말 잘했는데요? 상태 관리를 공부했군요. 난이도가 어려운데 잘 해낸 것 같아요. 앞으로도 화이팅해봐요!",
                    createdAt = 0
                ),
            )
        }
    }
}
