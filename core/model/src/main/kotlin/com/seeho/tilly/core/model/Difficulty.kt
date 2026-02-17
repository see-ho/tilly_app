package com.seeho.tilly.core.model

enum class Difficulty(
    val label: String,       // API/DB 매핑용
    val displayName: String  // 화면 표시용
) {
    EASY("EASY", "Easy"),
    NORMAL("NORMAL", "Normal"),
    HARD("HARD", "Hard"),
    VERY_HARD("VERY_HARD", "Very Hard");

    companion object {
        fun fromLabel(label: String): Difficulty {
            return entries.find { it.label.equals(label, ignoreCase = true) } ?: NORMAL
        }
    }
}
