package statistic.domain.dto

import java.time.LocalDate

data class DayStatisticDto(
    val day: LocalDate,
    val trainingsCount: Int,
)
