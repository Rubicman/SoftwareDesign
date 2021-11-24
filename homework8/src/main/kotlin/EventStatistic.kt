import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap

class EventStatistic(private val clock: Clock) {
    private val statistics = HashMap<String, LinkedList<StatisticData>>()
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    fun incEvent(name: String) {
        val time = clock.instant().minutePrecision()
        val statistic = statistics[name]
        if (statistic == null) {
            statistics[name] = LinkedList<StatisticData>().apply {
                push(StatisticData(time, 1))
            }
        } else {
            if (statistic.last.time == time) {
                statistic.last.count++
            } else {
                statistic.add(StatisticData(time, 1))
            }
        }
    }

    fun getStatisticByName(name: String): List<StatisticData>? {
        clean(name, clock.instant().minusHour().minutePrecision())
        return statistics[name]
    }

    fun getAllStatistic(): HashMap<String, out List<StatisticData>> {
        cleanAll(clock.instant().minusHour().minutePrecision())
        return statistics
    }

    fun printStatistic() {
        val instant = clock.instant()
        cleanAll(instant.minusHour().minutePrecision())
        println("Statistic for ${instant.minutePrecision()}")
        statistics.forEach { (name, statistics) ->
            println()
            println("-----$name${"-".repeat(20 - name.length)}")
            statistics.forEach { (time, count) ->
                println("[$time]: $count event${if (count > 1) "s" else ""}")
            }
        }
    }

    fun clear() {
        statistics.clear()
    }

    private fun cleanAll(time: String) {
        statistics.forEach { (key, _) -> clean(key, time) }
    }

    private fun clean(name: String, time: String) {
        val statistic = statistics[name] ?: return
        while (statistic.peek().time <= time) {
            statistic.poll()
        }
    }

    private fun Instant.minutePrecision(): String =
        formatter.format(ZonedDateTime.from(this.atZone(clock.zone)))

    private fun Instant.minusHour(): Instant =
        minus(Duration.ofHours(1))
}