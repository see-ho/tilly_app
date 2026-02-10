package com.seeho.tilly.core.data.mapper

import com.seeho.tilly.core.database.entity.TilEntity
import com.seeho.tilly.core.model.Til

/**
 * TilEntity ↔ Til 모델 간 변환 확장함수
 */

/** Entity → 도메인 모델 변환 */
fun TilEntity.toModel(): Til {
    // JSON 문자열을 List<String>으로 파싱
    val tagList = if (tags == "[]" || tags.isBlank()) {
        emptyList()
    } else {
        tags.removeSurrounding("[", "]")
            .split(",")
            .map { it.trim().removeSurrounding("\"") }
            .filter { it.isNotBlank() }
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
    // List<String>을 JSON 문자열로 변환
    val tagsJson = tags.joinToString(separator = ",") { "\"$it\"" }
        .let { "[$it]" }

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
