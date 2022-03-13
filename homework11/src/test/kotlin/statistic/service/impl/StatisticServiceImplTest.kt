package statistic.service.impl

import TestClock
import com.nhaarman.mockito_kotlin.*
import common.domain.event.EnterEvent
import common.domain.event.EnterEventType.ENTER
import common.domain.event.EnterEventType.EXIT
import common.repository.EnterRepository
import org.junit.jupiter.api.Test
import java.time.Duration.*
import java.time.LocalDate
import kotlin.test.assertEquals

internal class StatisticServiceImplTest {
    private val enterRepository = mock<EnterRepository>()
    private val clock = TestClock()
    private val service = StatisticServiceImpl(enterRepository, clock.time - ofHours(1), clock)

    @Test
    fun `empty by-day statistic`() {
        assertEquals(emptyMap(), service.byDay())

        verify(enterRepository, times(2)).get(null, clock.time - ofHours(1))
    }

    @Test
    fun `empty average statistic`() {
        assertEquals(ZERO to 0.0, service.averages())

        verify(enterRepository, times(2)).get(null, clock.time - ofHours(1))
    }

    @Test
    fun `by-day statistic`() {
        val enter1 = EnterEvent("1", ENTER, "1", clock.time)
        val exit1 = EnterEvent("2", EXIT, "1", clock.time + ofHours(2))
        val enter2 = EnterEvent("3", ENTER, "2", clock.time + ofMinutes(90))
        val exit2 = EnterEvent("4", EXIT, "2", clock.time + ofMinutes(150))
        val enter3 = EnterEvent("5", ENTER, "1", clock.time + ofDays(1) + ofHours(1))
        val exit3 = EnterEvent("6", EXIT, "1", clock.time + ofDays(1) + ofMinutes(150))
        clock.time += ofDays(1) + ofHours(3)

        whenever(enterRepository.get(anyOrNull(), anyOrNull())).thenReturn(listOf(enter1,
            enter2,
            enter3,
            exit1,
            exit2,
            exit3))

        val actual = service.byDay()

        assertEquals(mapOf(
            LocalDate.parse("2022-03-07") to 2,
            LocalDate.parse("2022-03-08") to 1,
        ), actual)

        verify(enterRepository, times(2)).get(null, enter1.time - ofHours(1))
    }

    @Test
    fun `average statistic`() {
        val enter1 = EnterEvent("1", ENTER, "1", clock.time)
        val exit1 = EnterEvent("2", EXIT, "1", clock.time + ofHours(2))
        val enter2 = EnterEvent("3", ENTER, "2", clock.time + ofMinutes(90))
        val exit2 = EnterEvent("4", EXIT, "2", clock.time + ofMinutes(150))
        val enter3 = EnterEvent("5", ENTER, "1", clock.time + ofDays(1) + ofHours(1))
        val exit3 = EnterEvent("6", EXIT, "1", clock.time + ofDays(1) + ofMinutes(150))
        clock.time += ofDays(1) + ofHours(3)

        whenever(enterRepository.get(anyOrNull(), anyOrNull())).thenReturn(listOf(enter1,
            enter2,
            enter3,
            exit1,
            exit2,
            exit3))

        val actual = service.averages()

        assertEquals(ofMinutes(90) to 1.5, actual)

        verify(enterRepository, times(2)).get(null, enter1.time - ofHours(1))
    }
}