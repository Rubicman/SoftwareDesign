package pass.service.impl

import TestClock
import com.nhaarman.mockito_kotlin.*
import common.domain.event.EnterEvent
import common.domain.event.EnterEventType.ENTER
import common.domain.event.EnterEventType.EXIT
import common.domain.event.PassEvent
import common.domain.event.PassEventType.CREATE
import common.repository.EnterRepository
import common.repository.PassRepository
import org.junit.jupiter.api.Test
import java.time.Duration
import java.util.*
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class EnterServiceImplTest {
    private val enterRepository = mock<EnterRepository>()
    private val passRepository = mock<PassRepository>()
    private val clock = TestClock()
    private val service = EnterServiceImpl(enterRepository, passRepository, clock)

    @Test
    fun `success enter`() {
        val uuid = UUID.randomUUID()
        val pass = PassEvent(
            "1",
            "2",
            "user id",
            "manager id",
            CREATE,
            clock.time - Duration.ofHours(1),
            Duration.ofHours(2)
        )
        whenever(enterRepository.nextUUID).thenReturn(uuid)
        whenever(passRepository.latest(any())).thenReturn(pass)

        assertTrue { service.enter("2") }

        verify(passRepository).latest("2")
        verify(enterRepository).save(EnterEvent(
            uuid.toString(),
            ENTER,
            "2",
            clock.time
        ))
    }

    @Test
    fun `forbidden enter`() {
        val uuid = UUID.randomUUID()
        val pass = PassEvent(
            "1",
            "2",
            "user id",
            "manager id",
            CREATE,
            clock.time - Duration.ofHours(2),
            Duration.ofHours(1)
        )
        whenever(enterRepository.nextUUID).thenReturn(uuid)
        whenever(passRepository.latest(any())).thenReturn(pass)

        assertFalse { service.enter("2") }

        verify(passRepository).latest("2")
        verifyNoMoreInteractions(enterRepository)
    }

    @Test
    fun `success exit`() {
        val uuid = UUID.randomUUID()
        val pass = PassEvent(
            "1",
            "2",
            "user id",
            "manager id",
            CREATE,
            clock.time - Duration.ofHours(1),
            Duration.ofHours(2)
        )
        whenever(enterRepository.nextUUID).thenReturn(uuid)
        whenever(passRepository.latest(any())).thenReturn(pass)

        assertTrue { service.exit("2") }

        verify(passRepository).latest("2")
        verify(enterRepository).save(EnterEvent(
            uuid.toString(),
            EXIT,
            "2",
            clock.time
        ))
    }

    @Test
    fun `forbidden exit`() {
        val uuid = UUID.randomUUID()
        val pass = PassEvent(
            "1",
            "2",
            "user id",
            "manager id",
            CREATE,
            clock.time - Duration.ofHours(2),
            Duration.ofHours(1)
        )
        whenever(enterRepository.nextUUID).thenReturn(uuid)
        whenever(passRepository.latest(any())).thenReturn(pass)

        assertFalse { service.exit("2") }

        verify(passRepository).latest("2")
        verifyNoMoreInteractions(enterRepository)
    }
}