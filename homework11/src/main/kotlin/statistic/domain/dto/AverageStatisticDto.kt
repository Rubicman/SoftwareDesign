package statistic.domain.dto

import java.time.Duration

data class AverageStatisticDto(
    val trainTime: Duration,
    val trains: Double
)
