package manager.domain.data

import java.time.Duration

data class RenewPassData(
    val managerId: String,
    val duration: Duration
)
