package com.seeho.tilly.core.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "retrospectives",
    indices = [Index(value = ["month", "year"], unique = true)]
)
data class RetrospectiveEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val month: Int,
    val year: Int,
    val summary: String,
    val growthPoints: List<String>,
    val improvementPoints: List<String>,
    val nextMonthSuggestions: List<String>,
    val createdAt: Long,
)
