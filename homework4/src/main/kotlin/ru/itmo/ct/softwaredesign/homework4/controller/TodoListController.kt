package ru.itmo.ct.softwaredesign.homework4.controller

import org.springframework.web.bind.annotation.*
import ru.itmo.ct.softwaredesign.homework4.domain.TodoList
import ru.itmo.ct.softwaredesign.homework4.service.TodoListService

@RestController
@RequestMapping("/todoList")
class TodoListController(
    override val service: TodoListService
): AbstractController<TodoList>() {

    @GetMapping("/{id}")
    override fun get(@PathVariable id: Long): TodoList = super.get(id)

    @DeleteMapping("/{id}")
    override fun delete(@PathVariable id: Long): TodoList = super.delete(id)

    @PostMapping
    fun post(@RequestParam name: String): TodoList {
        return service.create(name)
    }

    @GetMapping
    fun getBy(@RequestParam name: String?): List<TodoList> {
        return service.getBy(name)
    }
}