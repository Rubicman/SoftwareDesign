package domain.message

import java.time.Duration

data class TaskMessage(
    val query: String,
    val timeout: Duration
)
