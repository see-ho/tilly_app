package com.seeho.tilly.core.database.converter

import androidx.room.TypeConverter

/**
 * Room TypeConverter: List<String> ↔ JSON String 변환
 */
class TagListConverter {

    /**
     * List<String> → JSON String
     * 예: ["kotlin", "compose"] → "[\"kotlin\",\"compose\"]"
     */
    @TypeConverter
    fun fromTagList(tags: List<String>): String {
        return tags.joinToString(separator = ",") { "\"$it\"" }
            .let { "[$it]" }
    }

    /**
     * JSON String → List<String>
     * 예: "[\"kotlin\",\"compose\"]" → ["kotlin", "compose"]
     */
    @TypeConverter
    fun toTagList(tagsString: String): List<String> {
        if (tagsString == "[]" || tagsString.isBlank()) return emptyList()
        return tagsString
            .removeSurrounding("[", "]")
            .split(",")
            .map { it.trim().removeSurrounding("\"") }
            .filter { it.isNotBlank() }
    }
}
