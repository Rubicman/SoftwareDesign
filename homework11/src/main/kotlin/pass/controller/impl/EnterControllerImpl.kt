package pass.controller.impl

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import pass.controller.EnterController
import pass.service.EnterService

class EnterControllerImpl(
    private val enterService: EnterService,
): EnterController {
    override fun Route.enter(): Route = post("/enter") {
        if (enterService.enter(call.request.queryParameters["passId"].orEmpty())) {
            call.response.status(HttpStatusCode.OK)
        } else {
            call.response.status(HttpStatusCode.Forbidden)
        }
    }

    override fun Route.exit(): Route = post("/exit") {
        if (enterService.exit(call.request.queryParameters["passId"].orEmpty())) {
            call.response.status(HttpStatusCode.OK)
        } else {
            call.response.status(HttpStatusCode.Forbidden)
        }
    }

}