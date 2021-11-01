package ru.itmo.ct.softwaredesign.homework4.domain

data class TodoList(
    override var id: Long,
    var name: String,
): WithId {
    val todos: MutableList<Todo> = mutableListOf()
}
