package common.repository

import common.domain.event.EnterEvent
import java.time.ZonedDateTime
import java.util.*

interface EnterRepository {
    fun get(
        passId: String? = null,
        from: ZonedDateTime? = null,
    ): List<EnterEvent>

    fun save(event: EnterEvent): EnterEvent

    val nextUUID: UUID
}