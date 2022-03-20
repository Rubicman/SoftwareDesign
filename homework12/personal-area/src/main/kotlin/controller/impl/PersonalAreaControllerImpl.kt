package controller.impl

import controller.PersonalAreaController
import domain.dto.SummaryBalanceDto
import domain.dto.UserIdDto
import io.ktor.http.HttpStatusCode.Companion.NoContent
import io.ktor.http.HttpStatusCode.Companion.NotFound
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import service.PersonalAreaService

class PersonalAreaControllerImpl(
    private val personalAreaService: PersonalAreaService,
) : PersonalAreaController {
    override fun Route.addUser(): Route = post("/user") {
        call.respond(UserIdDto(personalAreaService.addUser(call.receive())))
    }

    override fun Route.changeBalance(): Route = post("/user/{id}/balance") {
        personalAreaService.changeBalance(call.id, call.receive())
        call.respond(NoContent)
    }

    override fun Route.getUser(): Route = get("/user/{id}") {
        call.respond(
            personalAreaService.getUser(
                call.id
            )?.dto ?: NotFound
        )
    }

    override fun Route.getUserStocks(): Route = get("/user/{id}/stocks") {
        call.respond(
            personalAreaService.getUserStocks(call.id).map { it.dto }
        )
    }

    override fun Route.getUserSummaryBalance(): Route = get("/user/{id}/balance") {
        call.respond(
            SummaryBalanceDto(personalAreaService.getUserSummaryBalance(call.id))
        )
    }

    override fun Route.buyStocks(): Route = post("/user/{id}/buy") {
        personalAreaService.buyStocks(call.id, call.receive())
        call.respond(NoContent)

    }

    override fun Route.sellStocks(): Route = post("/user/{id}/sell") {
        personalAreaService.sellStocks(call.id, call.receive())
        call.respond(NoContent)
    }

    private val ApplicationCall.id
        get() = parameters["id"] ?: ""
}