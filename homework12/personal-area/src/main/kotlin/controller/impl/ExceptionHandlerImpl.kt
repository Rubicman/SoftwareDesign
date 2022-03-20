package controller.impl

import controller.ExceptionHandler
import domain.exception.NoSuchUserException
import domain.exception.NotEnoughAmountOnBalance
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.InternalServerError
import io.ktor.http.HttpStatusCode.Companion.NotFound
import io.ktor.server.plugins.*
import io.ktor.server.response.*

class ExceptionHandlerImpl : ExceptionHandler {

    override fun StatusPagesConfig.noSuchUser() {
        exception<NoSuchUserException> { call, cause ->
            call.respond(NotFound)
            throw cause
        }
    }

    override fun StatusPagesConfig.notEnoughAmount() {
        exception<NotEnoughAmountOnBalance> { call, cause ->
            call.respond(BadRequest)
            throw cause
        }
    }

    override fun StatusPagesConfig.illegalArgument() {
        exception<IllegalArgumentException> { call, cause ->
            call.respond(BadRequest)
            throw cause
        }
    }

    override fun StatusPagesConfig.internalsErrors() {
        exception<Exception> { call, cause ->
            call.respond(InternalServerError)
            throw cause
        }
    }
}