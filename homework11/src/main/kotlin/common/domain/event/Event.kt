package common.domain.event

interface Event {
    val id: String

    fun toDto(): Any
}