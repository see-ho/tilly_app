package com.seeho.tilly.core.designsystem.util

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.seeho.tilly.core.designsystem.R
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

val Difficulty.iconRes: Int
    @DrawableRes get() = when (this) {
        Difficulty.EASY -> R.drawable.ic_difficult_easy
        Difficulty.NORMAL -> R.drawable.ic_difficult_normal
        Difficulty.HARD -> R.drawable.ic_difficult_hard
        Difficulty.VERY_HARD -> R.drawable.ic_difficult_veryhard
    }

val Emotion.color: Color
    get() = when (this) {
        Emotion.ACHIEVEMENT -> EmotionAchievement
        Emotion.SATISFACTION -> EmotionSatisfaction
        Emotion.NORMAL -> EmotionNormal
        Emotion.HARD -> EmotionHard
        Emotion.FRUSTRATION -> EmotionFrustration
    }
