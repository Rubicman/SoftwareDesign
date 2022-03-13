package manager.domain.data

import java.time.Duration

data class CreatePassData(
    val userId: String,
    val managerId: String,
    val duration: Duration
)