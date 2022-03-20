package controller.impl

import controller.ExchangeController
import domain.dto.CompanyIdDto
import domain.exception.NoSuchCompanyException
import domain.exception.NotEnoughStocksException
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.NoContent
import io.ktor.http.HttpStatusCode.Companion.NotFound
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import service.ExchangeService


class ExchangeControllerImpl(
    private val exchangeService: ExchangeService,
) : ExchangeController {
    override fun Route.addCompany(): Route = post("/company") {
        call.respond(CompanyIdDto(exchangeService.addCompany(call.receive())))
    }

    override fun Route.changeStocks(): Route = post("/company/{id}/stocks") {
        try {
            exchangeService.changeStock(call.id, call.receive())
            call.respond(NoContent)
        } catch (e: NoSuchCompanyException) {
            call.respond(NotFound)
        } catch (e: NotEnoughStocksException) {
            call.respond(BadRequest)
        }
    }

    override fun Route.getInfo(): Route = get("/company/{id}") {
        exchangeService.getInfo(call.id)?.let { company ->
            call.respond(company.toDto())
        } ?: call.respond(NotFound)
    }

    override fun Route.setPrice(): Route = post("/company/{id}/price") {
        try {
            exchangeService.setPrice(call.id, call.receive())
            call.respond(NoContent)
        } catch (e: NoSuchCompanyException) {
            call.respond(NotFound)
        } catch (e: IllegalArgumentException) {
            call.respond(BadRequest)
        }
    }

    private val ApplicationCall.id
        get() = parameters["id"] ?: ""
}