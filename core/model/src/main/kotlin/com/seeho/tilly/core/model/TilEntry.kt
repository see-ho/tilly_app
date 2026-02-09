package com.seeho.tilly.core.model

data class TilEntry(
    val id: Long,
    val date: String,
    val title: String,
    val emotionScore: Int,
    val tags: List<String>,
    val content: String
)
