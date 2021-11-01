package ru.itmo.ct.softwaredesign.homework4.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import ru.itmo.ct.softwaredesign.homework4.domain.Todo
import ru.itmo.ct.softwaredesign.homework4.repository.TodoRepository
import java.time.ZonedDateTime

@Service
class TodoService: EntityService<Todo> {

    @Autowired
    lateinit var todoRepository: TodoRepository

    @Lazy
    @Autowired
    lateinit var todoListService: TodoListService

    override fun get(id: Long): Todo? = todoRepository.get(id)

    fun add(name: String, todoListId: Long?): Todo {
        val todoList = if (todoListId != null) {
            todoListService.get(todoListId)
        } else {
            null
        }
        val todo = Todo(
            id = -1,
            name = name,
            todoList = todoList
        )
        todoRepository.save(todo)
        if (todoList != null) {
            todoListService.addTodoToList(todoList, todo)
        }
        return todo
    }

    override fun delete(id: Long): Todo? {
        return todoRepository.get(id)?.also { todo ->
            todoRepository.delete(id)
            todo.todoList?.also { todoList ->
                todoListService.removeTodoFromList(todoList, todo)
            }
        }
    }

    fun changeStatus(id: Long): Todo? {
        return todoRepository.get(id)?.also {
            it.completed = !it.completed
            todoRepository.save(it)
        }
    }

    fun getBy(
        name: String?,
        completed: Boolean?,
        fromCreatedTime: ZonedDateTime?,
        toCreatedTime: ZonedDateTime?
    ): List<Todo> {
        return todoRepository.getBy(name, completed, fromCreatedTime, toCreatedTime)
    }
}