package common.repository

import common.domain.event.PassEvent
import common.domain.event.PassEventType
import java.util.*

interface PassRepository {
    fun get(
        passId: String? = null,
        type: PassEventType? = null
    ): List<PassEvent>

    fun latest(passId: String?): PassEvent?

    fun save(event: PassEvent): PassEvent

    val nextUUID: UUID
}