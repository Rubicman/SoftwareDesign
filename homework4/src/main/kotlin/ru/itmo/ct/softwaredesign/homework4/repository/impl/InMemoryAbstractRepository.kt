package ru.itmo.ct.softwaredesign.homework4.repository.impl

import ru.itmo.ct.softwaredesign.homework4.domain.WithId
import ru.itmo.ct.softwaredesign.homework4.repository.AbstractRepository

abstract class InMemoryAbstractRepository<T>: AbstractRepository<T> where T : WithId {
    protected val idToElement: MutableMap<Long, T> = mutableMapOf()
    private var nextId: Long = 1L

    override fun get(id: Long): T? {
        return idToElement[id]
    }

    override fun save(element: T) {
        if (element.id <= 0) {
            element.id = nextId++
        }
        idToElement[element.id] = element
    }

    override fun delete(id: Long) {
        idToElement.remove(id)
    }

}