package ru.itmo.ct.softwaredesign.homework4.repository.impl

import org.springframework.stereotype.Repository
import ru.itmo.ct.softwaredesign.homework4.domain.Todo
import ru.itmo.ct.softwaredesign.homework4.repository.TodoRepository
import java.time.ZonedDateTime

@Repository
class InMemoryTodoRepository : InMemoryAbstractRepository<Todo>(), TodoRepository {
    override fun getBy(
        name: String?,
        completed: Boolean?,
        fromCreatedTime: ZonedDateTime?,
        toCreatedTime: ZonedDateTime?
    ): List<Todo> {
        return idToElement.values
            .filter(filter(name, completed, fromCreatedTime, toCreatedTime))
            .toList()
    }

    private fun filter(
        name: String?,
        completed: Boolean?,
        fromCreatedTime: ZonedDateTime?,
        toCreatedTime: ZonedDateTime?
    ): (Todo) -> Boolean = {
        when {
            name != null && it.name != name -> false
            completed != null && it.completed != completed -> false
            fromCreatedTime != null && it.creationTime.isBefore(fromCreatedTime) -> false
            toCreatedTime != null && it.creationTime.isAfter(toCreatedTime) -> false
            else -> true
        }
    }
}