package statistic.service

import java.time.Duration
import java.time.LocalDate

interface StatisticService {
    fun byDay(): Map<LocalDate, Int>
    fun averages(): Pair<Duration, Double>
}