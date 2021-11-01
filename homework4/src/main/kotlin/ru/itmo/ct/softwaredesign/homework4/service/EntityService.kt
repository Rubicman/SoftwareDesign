package ru.itmo.ct.softwaredesign.homework4.service

interface EntityService<T> {
    fun get(id: Long): T?

    fun delete(id: Long): T?
}