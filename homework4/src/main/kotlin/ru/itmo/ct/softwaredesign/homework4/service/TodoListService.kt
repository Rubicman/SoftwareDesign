package ru.itmo.ct.softwaredesign.homework4.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import ru.itmo.ct.softwaredesign.homework4.domain.Todo
import ru.itmo.ct.softwaredesign.homework4.domain.TodoList
import ru.itmo.ct.softwaredesign.homework4.repository.TodoListRepository

@Service
class TodoListService : EntityService<TodoList> {

    @Autowired
    lateinit var todoListRepository: TodoListRepository

    @Lazy
    @Autowired
    lateinit var todoService: TodoService

    override fun get(id: Long): TodoList? {
        return todoListRepository.get(id)
    }

    fun addTodoToList(todoList: TodoList, todo: Todo) {
        todoList.todos.add(todo)
        todoListRepository.save(todoList)
    }

    fun removeTodoFromList(todoList: TodoList, todo: Todo) {
        todoList.todos.remove(todo)
        todoListRepository.save(todoList)
    }

    override fun delete(id: Long): TodoList? {
        return todoListRepository.get(id)?.also { todoList ->
            todoList.todos.forEach { todo ->
                todo.todoList = null
                todoService.delete(todo.id)
            }
            todoListRepository.delete(id)
        }
    }

    fun getBy(
        name: String? = null
    ): List<TodoList> {
        return todoListRepository.getBy(name)
    }

    fun create(name: String): TodoList {
        val todoList = TodoList(
            id = -1,
            name = name
        )
        todoListRepository.save(todoList)
        return todoList
    }
}