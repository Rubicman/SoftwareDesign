package ru.itmo.ct.softwaredesign.homework4.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.ZonedDateTime

data class Todo(
    override var id: Long,
    var name: String,
    var creationTime: ZonedDateTime = ZonedDateTime.now(),
    @JsonIgnore var todoList: TodoList? = null
) : WithId {
    var completed = false

    val todoListId: Long?
        get() = todoList?.id
}

