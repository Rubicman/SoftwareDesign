package manager.service.impl

import common.domain.Pass
import common.domain.event.Event
import common.domain.event.PassEvent
import common.domain.event.PassEventType.CREATE
import common.domain.event.PassEventType.RENEW
import common.repository.PassRepository
import manager.domain.data.CreatePassData
import manager.domain.data.RenewPassData
import manager.service.PassService
import java.time.Clock
import java.time.ZonedDateTime

class PassServiceImpl(
    private val passRepository: PassRepository,
    private val clock: Clock
) : PassService {
    override fun getEvents(passId: String): List<Event> =
        passRepository.get(passId = passId)

    override fun createPass(data: CreatePassData): Pass {
        val event = passRepository.save(
            PassEvent(
                passRepository.nextUUID.toString(),
                passRepository.nextUUID.toString(),
                data.userId,
                data.managerId,
                CREATE,
                ZonedDateTime.now(clock),
                data.duration
            )
        )
        return Pass(
            event.passId,
            event.userId,
            event.time,
            event.time + event.duration
        )
    }

    override fun renew(passId: String, data: RenewPassData): Pass {
        val createEvent = passRepository.get(
            passId = passId,
            type = CREATE
        ).firstOrNull() ?: throw RuntimeException("No pass with such id")
        val renewEvent = passRepository.save(
            PassEvent(
                passRepository.nextUUID.toString(),
                passId,
                createEvent.userId,
                data.managerId,
                RENEW,
                ZonedDateTime.now(clock),
                data.duration
            )
        )
        return Pass(
            createEvent.passId,
            createEvent.userId,
            createEvent.time,
            renewEvent.time + renewEvent.duration
        )
    }
}