package com.seeho.tilly.core.common.util

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateUtils {
    private const val PATTERN_DOT_DATE = "yyyy.MM.dd"
    private const val PATTERN_DOT_DATE_TIME = "yyyy.MM.dd hh:mm a"

    fun formatToDotDate(timestamp: Long): String {
        val formatter = DateTimeFormatter.ofPattern(PATTERN_DOT_DATE, Locale.getDefault())
        return Instant.ofEpochMilli(timestamp)
            .atZone(ZoneId.systemDefault())
            .format(formatter)
    }

    fun formatToDotDateTime(timestamp: Long): String {
        val formatter = DateTimeFormatter.ofPattern(PATTERN_DOT_DATE_TIME, Locale.getDefault())
        return Instant.ofEpochMilli(timestamp)
            .atZone(ZoneId.systemDefault())
            .format(formatter)
    }

    /** timestamp(millis)를 LocalDate로 변환 (시스템 기본 시간대 사용) */
    fun timestampToLocalDate(timestamp: Long): LocalDate {
        return Instant.ofEpochMilli(timestamp)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }
}
