package dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.ZonedDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class VkRecord(
    val id: Long,
    val date: ZonedDateTime
)