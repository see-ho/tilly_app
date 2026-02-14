package com.seeho.tilly.core.model

enum class Emotion(
    val label: String
) {
    ACHIEVEMENT("성취감"),
    SATISFACTION("만족"),
    NORMAL("평범"),
    HARD("어려움"),
    FRUSTRATION("좌절");

    companion object {
        fun fromLabel(label: String): Emotion {
            return entries.find { it.label == label } ?: NORMAL
        }
    }
}
