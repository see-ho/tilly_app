package com.seeho.tilly.core.data.mapper

import com.seeho.tilly.core.database.entity.TilEntity
import com.seeho.tilly.core.model.Til
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * TilEntity ↔ Til 모델 간 변환 확장함수
 */

private val json = Json { ignoreUnknownKeys = true }

/** Entity → 도메인 모델 변환 */
fun TilEntity.toModel(): Til {
    val tagList = try {
        if (tags.isBlank()) emptyList() else json.decodeFromString<List<String>>(tags)
    } catch (e: Exception) {
        emptyList()
    }

    return Til(
        id = id,
        title = title,
        learned = learned,
        difficulty = difficulty,
        tomorrow = tomorrow,
        tags = tagList,
        emotion = emotion,
        emotionScore = emotionScore,
        difficultyLevel = difficultyLevel,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}

/** 도메인 모델 → Entity 변환 */
fun Til.toEntity(): TilEntity {
    val tagsJson = json.encodeToString(tags)

    return TilEntity(
        id = id,
        title = title,
        learned = learned,
        difficulty = difficulty,
        tomorrow = tomorrow,
        tags = tagsJson,
        emotion = emotion,
        emotionScore = emotionScore,
        difficultyLevel = difficultyLevel,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}
