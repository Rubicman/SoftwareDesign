package manager.controller

import io.ktor.server.routing.*

interface PassController {
    fun Route.getEvents(): Route
    fun Route.createPass(): Route
    fun Route.renewPass(): Route
}