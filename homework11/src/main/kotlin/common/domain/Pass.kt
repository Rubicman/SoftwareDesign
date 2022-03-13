package common.domain

import java.time.ZonedDateTime

data class Pass(
    val id: String,
    val userId: String,
    val creationTime: ZonedDateTime,
    val expirationTime: ZonedDateTime,
)