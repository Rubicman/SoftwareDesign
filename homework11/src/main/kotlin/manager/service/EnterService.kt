package manager.service

import common.domain.event.Event

interface EnterService {
    fun getEvents(passId: String): List<Event>
}