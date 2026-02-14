package com.seeho.tilly.core.designsystem.util

import androidx.compose.ui.graphics.Color
import com.seeho.tilly.core.designsystem.theme.*
import com.seeho.tilly.core.model.Difficulty
import com.seeho.tilly.core.model.Emotion

val Difficulty.color: Color
    get() = when (this) {
        Difficulty.EASY -> DifficultyEasy
        Difficulty.NORMAL -> DifficultyNormal
        Difficulty.HARD -> DifficultyHard
        Difficulty.VERY_HARD -> DifficultyVeryHard
    }

val Emotion.color: Color
    get() = when (this) {
        Emotion.ACHIEVEMENT -> EmotionAchievement
        Emotion.SATISFACTION -> EmotionSatisfaction
        Emotion.NORMAL -> EmotionNormal
        Emotion.HARD -> EmotionHard
        Emotion.FRUSTRATION -> EmotionFrustration
    }
