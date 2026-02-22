package com.seeho.tilly.core.domain

import com.seeho.tilly.core.domain.repository.TilRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

/**
 * 이번 주(월~일) TIL 작성 여부를 반환하는 UseCase
 * 이번 주 범위만 DB에서 조회
 */
class GetWeeklyTilCheckUseCase @Inject constructor(
    private val tilRepository: TilRepository,
) {
    operator fun invoke(): Flow<List<Boolean>> {
        val today = LocalDate.now()
        val zone = ZoneId.systemDefault()

        // 이번 주 월요일~일요일 구하기
        val monday = today.with(DayOfWeek.MONDAY)
        val sunday = monday.plusDays(6)

        // 월요일~일요일까지의 날짜 목록
        val weekDates = (0L..6L).map { monday.plusDays(it) }

        // epoch millis 범위 계산 (월요일 00:00:00 ~ 일요일 23:59:59.999)
        val startMillis = monday.atStartOfDay(zone).toInstant().toEpochMilli()
        val endMillis = sunday.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli() - 1

        return tilRepository.getTilsBetween(startMillis, endMillis).map { tils ->
            // TIL의 createdAt을 LocalDate로 변환하여 Set으로 수집
            val tilDates = tils.map { til ->
                Instant.ofEpochMilli(til.createdAt)
                    .atZone(zone)
                    .toLocalDate()
            }.toSet()

            // 각 요일에 TIL이 있는지 체크
            weekDates.map { date -> date in tilDates }
        }
    }
}
