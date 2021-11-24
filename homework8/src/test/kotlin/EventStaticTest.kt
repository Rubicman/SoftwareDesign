import org.junit.Before
import org.junit.Test
import java.time.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class EventStaticTest {
    private val baseInstant = ZonedDateTime.of(2007, 5, 10, 10, 0, 0, 13, ZoneId.of("UTC")).toInstant()
    private val mockClock = MockClock()
    private val eventStatistic = EventStatistic(mockClock)

    @Before
    fun before() {
        mockClock.instant = baseInstant
        eventStatistic.clear()
    }

    @Test
    fun `count event at same time`() {
        repeat(4) { eventStatistic.incEvent("event") }

        val statisticData = eventStatistic.getStatisticByName("event")?.first()
        assertNotNull(statisticData)
        assertEquals(4, statisticData.count)
        assertEquals("2007-05-10 10:00", statisticData.time)
    }

    @Test
    fun `count events at same time`() {
        repeat(3) { eventStatistic.incEvent("event1") }
        repeat(1) { eventStatistic.incEvent("event2") }
        repeat(2) { eventStatistic.incEvent("event3") }

        val allStatistics = eventStatistic.getAllStatistic()

        var statisticData: StatisticData? = allStatistics["event1"]?.first()
        assertNotNull(statisticData)
        assertEquals(3, statisticData.count)
        assertEquals("2007-05-10 10:00", statisticData.time)

        statisticData = allStatistics["event2"]?.first()
        assertNotNull(statisticData)
        assertEquals(1, statisticData.count)
        assertEquals("2007-05-10 10:00", statisticData.time)

        statisticData = allStatistics["event3"]?.first()
        assertNotNull(statisticData)
        assertEquals(2, statisticData.count)
        assertEquals("2007-05-10 10:00", statisticData.time)
    }

    @Test
    fun `count event at different times`() {
        mockClock.instant += Duration.ofMinutes(3)
        repeat(2) { eventStatistic.incEvent("event") }

        mockClock.instant += Duration.ofMinutes(4) + Duration.ofSeconds(40)
        repeat(7) { eventStatistic.incEvent("event") }

        mockClock.instant += Duration.ofSeconds(30)
        repeat(5) { eventStatistic.incEvent("event") }

        val statistic = eventStatistic.getStatisticByName("event")
        assertNotNull(statistic)
        assertEquals(3, statistic.size)

        var statisticData: StatisticData? = statistic[0]
        assertNotNull(statisticData)
        assertEquals(2, statisticData.count)
        assertEquals("2007-05-10 10:03", statisticData.time)

        statisticData = statistic[1]
        assertNotNull(statisticData)
        assertEquals(7, statisticData.count)
        assertEquals("2007-05-10 10:07", statisticData.time)

        statisticData = statistic[2]
        assertNotNull(statisticData)
        assertEquals(5, statisticData.count)
        assertEquals("2007-05-10 10:08", statisticData.time)
    }

    @Test
    fun `clean statistic after hour`() {
        repeat(4) { eventStatistic.incEvent("event") }

        mockClock.instant += Duration.ofMinutes(1)
        repeat(6) { eventStatistic.incEvent("event") }

        mockClock.instant += Duration.ofMinutes(59)
        repeat(3) { eventStatistic.incEvent("event") }

        var statistic = eventStatistic.getStatisticByName("event")
        assertNotNull(statistic)
        assertEquals(2, statistic.size)

        var statisticData: StatisticData? = statistic[0]
        assertNotNull(statisticData)
        assertEquals(6, statisticData.count)
        assertEquals("2007-05-10 10:01", statisticData.time)

        statisticData = statistic[1]
        assertNotNull(statisticData)
        assertEquals(3, statisticData.count)
        assertEquals("2007-05-10 11:00", statisticData.time)

        mockClock.instant += Duration.ofMinutes(1)
        statistic = eventStatistic.getStatisticByName("event")
        assertNotNull(statistic)
        assertEquals(1, statistic.size)

        statisticData = statistic[0]
        assertNotNull(statisticData)
        assertEquals(3, statisticData.count)
        assertEquals("2007-05-10 11:00", statisticData.time)
    }

    private class MockClock : Clock() {

        var instant: Instant = Instant.now()

        override fun instant(): Instant = instant

        override fun getZone(): ZoneId = ZoneId.of("UTC")

        override fun withZone(zone: ZoneId?): Clock {
            throw NotImplementedError()
        }
    }
}