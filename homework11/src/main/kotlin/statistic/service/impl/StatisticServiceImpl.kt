package statistic.service.impl

import common.domain.event.EnterEvent
import common.domain.event.EnterEventType.ENTER
import common.domain.event.EnterEventType.EXIT
import common.repository.EnterRepository
import statistic.service.StatisticService
import java.time.Clock
import java.time.Duration
import java.time.LocalDate
import java.time.ZonedDateTime

class StatisticServiceImpl(
    private val enterRepository: EnterRepository,
    private val from: ZonedDateTime,
    private val clock: Clock,
) : StatisticService {

    private val dayStatistic = mutableMapOf<LocalDate, Int>()
    private var enters = emptyList<EnterEvent>()
    private var trainings = 0L
    private var sumDuration = Duration.ZERO
    private var last = from

    init {
        update()
    }

    override fun byDay(): Map<LocalDate, Int> {
        update()
        return dayStatistic
    }

    override fun averages(): Pair<Duration, Double> {
        update()
        return if (trainings == 0L) {
            sumDuration to 0.0
        } else {
            sumDuration.dividedBy(trainings) to
                    trainings.toDouble() / (Duration.between(from, ZonedDateTime.now(clock)).toDays() + 1)
        }
    }

    @Synchronized
    private fun update() {
        val events = enterRepository.get(from = last)
        val zone = from.zone
        events.maxByOrNull { it.time }?.let { last = ZonedDateTime.ofInstant(it.time.toInstant(), zone) }
        val used = mutableSetOf<String>()
        (events + enters).groupBy { it.passId }
            .mapValues { (_, es) -> es.sortedBy { it.time } }
            .forEach { (_, es) ->
                for (i in 0 until es.size - 1) {
                    if (es[i].type == ENTER && es[i + 1].type == EXIT) {
                        sumDuration += Duration.between(es[i].time, es[i + 1].time)
                        trainings++
                        used.add(es[i].id)
                        used.add(es[i + 1].id)
                        val date = LocalDate.from(es[i].time)
                        dayStatistic[date] = dayStatistic.getOrDefault(date, 0) + 1
                    }
                }
            }
        enters = (events + enters).filter { it.id !in used }
    }
}