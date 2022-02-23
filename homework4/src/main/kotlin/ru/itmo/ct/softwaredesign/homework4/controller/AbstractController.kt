package ru.itmo.ct.softwaredesign.homework4.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.server.ResponseStatusException
import ru.itmo.ct.softwaredesign.homework4.service.EntityService

abstract class AbstractController<T> {
    abstract val service: EntityService<T>

    open fun get(@PathVariable id: Long): T = withNotFound {
        service.get(id)
    }

    open fun delete(@PathVariable id: Long): T = withNotFound {
        service.delete(id)
    }

    protected fun withNotFound(body: () -> T?): T {
        val result = body()
        if (result == null) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "No todo with such id")
        } else {
            return result
        }
    }
}