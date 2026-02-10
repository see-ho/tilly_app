package com.seeho.tilly.core.database.converter

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Room TypeConverter: List<String> ↔ JSON String 변환
 */
class TagListConverter {

    private val json = Json { ignoreUnknownKeys = true }

    /**
     * List<String> → JSON String
     */
    @TypeConverter
    fun fromTagList(tags: List<String>): String {
        return json.encodeToString(tags)
    }

    /**
     * JSON String → List<String>
     */
    @TypeConverter
    fun toTagList(tagsString: String): List<String> {
        if (tagsString.isBlank()) return emptyList()
        return try {
            json.decodeFromString(tagsString)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
