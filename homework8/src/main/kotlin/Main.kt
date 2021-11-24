import java.time.Clock

fun main() {
    val statistic = EventStatistic(Clock.systemDefaultZone())
    statistic.incEvent("foo")
    statistic.incEvent("foo")
    statistic.incEvent("foo")
    statistic.incEvent("bar")

    statistic.printStatistic()
}