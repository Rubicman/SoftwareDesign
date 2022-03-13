package manager.service.impl

import TestClock
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import common.domain.Pass
import common.domain.event.PassEvent
import common.domain.event.PassEventType.CREATE
import common.domain.event.PassEventType.RENEW
import common.repository.PassRepository
import manager.domain.data.CreatePassData
import manager.domain.data.RenewPassData
import org.junit.jupiter.api.Test
import java.time.Duration
import java.util.*
import kotlin.test.assertEquals

internal class PassServiceImplTest {
    private val passRepository = mock<PassRepository>()
    private val clock = TestClock()
    private val service = PassServiceImpl(passRepository, clock)

    @Test
    fun `create pass`() {
        val uuid1 = UUID.randomUUID()
        val uuid2 = UUID.randomUUID()

        val event = PassEvent(
            uuid1.toString(),
            uuid2.toString(),
            "user id",
            "manager id",
            CREATE,
            clock.time,
            Duration.ofHours(5)
        )

        whenever(passRepository.nextUUID)
            .thenReturn(uuid1)
            .thenReturn(uuid2)
        whenever(passRepository.save(any())).thenReturn(event)

        val actual = service.createPass(CreatePassData(
            "user id",
            "manager id",
            Duration.ofHours(5)
        ))

        assertEquals(Pass(
            uuid2.toString(),
            "user id",
            clock.time,
            clock.time + Duration.ofHours(5)
        ), actual)
        verify(passRepository).save(event)
    }

    @Test
    fun `renew pass`() {
        val uuid = UUID.randomUUID()
        val createEvent = PassEvent(
            "1",
            "2",
            "user id",
            "manager id",
            CREATE,
            clock.time,
            Duration.ofHours(5)
        )
        clock.time += Duration.ofHours(1)
        val renewEvent = PassEvent(
            uuid.toString(),
            "2",
            "user id",
            "manager id",
            RENEW,
            clock.time,
            Duration.ofHours(7)
        )
        whenever(passRepository.nextUUID).thenReturn(uuid)
        whenever(passRepository.get(any(), any())).thenReturn(listOf(createEvent))
        whenever(passRepository.save(any())).thenReturn(renewEvent)

        val actual = service.renew(
            "2",
            RenewPassData("manager id", Duration.ofHours(7))
        )

        assertEquals(Pass(
            "2",
            "user id",
            clock.time - Duration.ofHours(1),
            clock.time + Duration.ofHours(7)
        ), actual)

        verify(passRepository).get("2", CREATE)
        verify(passRepository).save(renewEvent)
    }
}