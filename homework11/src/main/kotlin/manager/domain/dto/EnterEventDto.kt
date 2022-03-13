package manager.domain.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.ZonedDateTime

data class EnterEventDto(
    val passId: String,
    val type: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z")
    val time: ZonedDateTime,
)
