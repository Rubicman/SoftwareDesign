package common.domain.event

import com.fasterxml.jackson.annotation.JsonProperty
import manager.domain.dto.EnterEventDto
import java.time.ZonedDateTime

data class EnterEvent(
    @JsonProperty("_id") override val id: String,
    val type: EnterEventType,
    val passId: String,
    val time: ZonedDateTime
): Event {
    override fun toDto() =
        EnterEventDto(
            passId,
            type.toString(),
            time
        )
}
