package ru.itmo.ct.softwaredesign.homework4.repository.impl

import org.springframework.stereotype.Repository
import ru.itmo.ct.softwaredesign.homework4.domain.TodoList
import ru.itmo.ct.softwaredesign.homework4.repository.TodoListRepository

@Repository
class InMemoryTodoListRepository : InMemoryAbstractRepository<TodoList>(), TodoListRepository {
    override fun getBy(name: String?): List<TodoList> {
        return idToElement.values
            .filter(filter(name))
            .toList()
    }

    private fun filter(name: String?): (TodoList) -> Boolean = {
        name == null || it.name == name
    }
}