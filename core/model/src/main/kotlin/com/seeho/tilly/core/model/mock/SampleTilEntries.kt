package com.seeho.tilly.core.model.mock

import com.seeho.tilly.core.model.TilEntry

val sampleTilEntries = listOf(
    TilEntry(
        id = 1,
        date = "2026.02.09",
        title = "Jetpack Compose Animation",
        emotionScore = 5,
        tags = listOf("compose", "animation"),
        content = "Today I learned how to use Animatable to create smooth transitions..."
    ),
    TilEntry(
        id = 2,
        date = "2026.02.08",
        title = "Hilt Dependency Injection",
        emotionScore = 4,
        tags = listOf("hilt", "di"),
        content = "Mastering Hilt made my code much cleaner and more testable."
    ),
    TilEntry(
        id = 3,
        date = "2026.02.07",
        title = "Basic Kotlin",
        emotionScore = 2,
        tags = listOf("kotlin"),
        content = "Just refreshing some basic Kotlin concepts today."
    )
)
