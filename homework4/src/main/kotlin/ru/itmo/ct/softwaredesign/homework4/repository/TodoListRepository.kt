package ru.itmo.ct.softwaredesign.homework4.repository

import org.springframework.stereotype.Repository
import ru.itmo.ct.softwaredesign.homework4.domain.TodoList

@Repository
interface TodoListRepository : AbstractRepository<TodoList> {
    fun getBy(
        name: String? = null
    ): List<TodoList>
}