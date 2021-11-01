package ru.itmo.ct.softwaredesign.homework4.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import ru.itmo.ct.softwaredesign.homework4.domain.Todo
import ru.itmo.ct.softwaredesign.homework4.service.TodoService
import java.time.ZonedDateTime

@RestController
@RequestMapping("/todo")
class TodoController(
    override val service: TodoService
) : AbstractController<Todo>() {

    @GetMapping("/{id}")
    override fun get(@PathVariable id: Long): Todo = super.get(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun post(@RequestParam name: String, @RequestParam todoListId: Long?): Todo {
        return service.add(name, todoListId)
    }

    @PatchMapping("/{id}")
    fun patch(@PathVariable id: Long): Todo = withNotFound {
        service.changeStatus(id)
    }

    @DeleteMapping("/{id}")
    override fun delete(@PathVariable id: Long): Todo = super.delete(id)

    @GetMapping
    fun getBy(
        @RequestParam name: String?,
        @RequestParam completed: Boolean?,
        @RequestParam fromCreatedTime: ZonedDateTime?,
        @RequestParam toCreatedTime: ZonedDateTime?
    ): List<Todo> {
        return service.getBy(name, completed, fromCreatedTime, toCreatedTime)
    }
}