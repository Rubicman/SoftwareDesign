package common.domain.event

import com.fasterxml.jackson.annotation.JsonProperty
import manager.domain.dto.PassEventDto
import java.time.Duration
import java.time.ZonedDateTime

data class PassEvent(
    @JsonProperty("_id") override val id: String,
    val passId: String,
    val userId: String,
    val managerId: String,
    val type: PassEventType,
    val time: ZonedDateTime,
    val duration: Duration,
) : Event {
    override fun toDto() =
        PassEventDto(
            userId,
            managerId,
            type.toString(),
            time,
            duration
        )
}
