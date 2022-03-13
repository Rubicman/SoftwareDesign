package pass.controller

import io.ktor.server.routing.*

interface EnterController {
    fun Route.enter(): Route
    fun Route.exit(): Route
}