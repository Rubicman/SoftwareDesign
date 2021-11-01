package ru.itmo.ct.softwaredesign.homework4.repository

interface AbstractRepository<T> {
    fun get(id: Long): T?

    fun save(element: T)

    fun delete(id: Long)
}