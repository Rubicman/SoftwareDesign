package ru.itmo.ct.softwaredesign.homework4.repository

import org.springframework.stereotype.Repository
import ru.itmo.ct.softwaredesign.homework4.domain.Todo
import java.time.ZonedDateTime

@Repository
interface TodoRepository : AbstractRepository<Todo> {
    fun getBy(
        name: String? = null,
        completed: Boolean? = null,
        fromCreatedTime: ZonedDateTime? = null,
        toCreatedTime: ZonedDateTime? = null
    ): List<Todo>
}