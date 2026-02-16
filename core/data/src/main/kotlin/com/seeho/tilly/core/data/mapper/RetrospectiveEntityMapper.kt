package com.seeho.tilly.core.data.mapper

import com.seeho.tilly.core.database.entity.RetrospectiveEntity
import com.seeho.tilly.core.model.MonthlyRetrospective

fun RetrospectiveEntity.toModel(): MonthlyRetrospective {
    return MonthlyRetrospective(
        id = id,
        month = month,
        year = year,
        summary = summary,
        growthPoints = growthPoints,
        improvementPoints = improvementPoints,
        nextMonthSuggestions = nextMonthSuggestions,
        createdAt = createdAt,
    )
}

fun MonthlyRetrospective.toEntity(): RetrospectiveEntity {
    return RetrospectiveEntity(
        month = month,
        year = year,
        summary = summary,
        growthPoints = growthPoints,
        improvementPoints = improvementPoints,
        nextMonthSuggestions = nextMonthSuggestions,
        createdAt = createdAt,
    )
}
