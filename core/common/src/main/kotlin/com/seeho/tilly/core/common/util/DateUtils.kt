package com.seeho.tilly.core.common.util

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Locale

object DateUtils {
    private const val PATTERN_DOT_DATE = "yyyy.MM.dd"
    private const val PATTERN_DOT_DATE_TIME = "yyyy.MM.dd HH:mm a"

    fun formatToDotDate(timestamp: Long): String {
        val formatter = SimpleDateFormat(PATTERN_DOT_DATE, Locale.getDefault())
        return formatter.format(Date(timestamp))
    }

    fun formatToDotDateTime(timestamp: Long): String {
        val formatter = SimpleDateFormat(PATTERN_DOT_DATE_TIME, Locale.getDefault())
        return formatter.format(Date(timestamp))
    }

    /** timestamp(millis)를 LocalDate로 변환 (시스템 기본 시간대 사용) */
    fun timestampToLocalDate(timestamp: Long): LocalDate {
        return Instant.ofEpochMilli(timestamp)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }
}
