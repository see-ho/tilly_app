package com.seeho.tilly.core.network.model

data class RetrospectiveResult(
    val summary: String,
    val growthPoints: List<String>,
    val improvementPoints: List<String>,
    val nextMonthSuggestions: List<String>,
)
