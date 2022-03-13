package pass.service.impl

import common.domain.event.EnterEvent
import common.domain.event.EnterEventType
import common.repository.EnterRepository
import common.repository.PassRepository
import pass.service.EnterService
import java.time.Clock
import java.time.ZonedDateTime

class EnterServiceImpl(
    private val enterRepository: EnterRepository,
    private val passRepository: PassRepository,
    private val clock: Clock,
) : EnterService {
    override fun enter(passId: String): Boolean =
        enterOrExit(passId, EnterEventType.ENTER)

    override fun exit(passId: String): Boolean =
        enterOrExit(passId, EnterEventType.EXIT)

    private fun enterOrExit(passId: String, type: EnterEventType): Boolean {
        val pass = passRepository.latest(passId) ?: return false
        if (pass.time + pass.duration < ZonedDateTime.now(clock)) return false
        enterRepository.save(EnterEvent(
            enterRepository.nextUUID.toString(),
            type,
            passId,
            ZonedDateTime.now(clock)
        ))
        return true
    }
}