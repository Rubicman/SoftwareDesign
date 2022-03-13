package manager.service.impl

import common.domain.event.Event
import common.repository.EnterRepository
import manager.service.EnterService

class EnterServiceImpl(
    private val enterRepository: EnterRepository,
) : EnterService {
    override fun getEvents(passId: String): List<Event> =
        enterRepository.get(passId = passId)
}