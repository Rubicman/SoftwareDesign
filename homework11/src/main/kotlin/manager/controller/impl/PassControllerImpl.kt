package manager.controller.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import common.domain.event.Event
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import manager.controller.PassController
import manager.domain.data.CreatePassData
import manager.domain.data.RenewPassData
import manager.service.EnterService
import manager.service.PassService

class PassControllerImpl(
    private val passService: PassService,
    private val enterService: EnterService,
    private val mapper: ObjectMapper,
) : PassController {
    override fun Route.getEvents(): Route = get("/pass/{passId}") {
        val events = call.parameters["passId"]?.let { passId ->
            passService.getEvents(passId) + enterService.getEvents(passId)
        }?.map(Event::toDto) ?: emptyList()
        call.respondText(ContentType.Application.Json, HttpStatusCode.OK) { mapper.writeValueAsString(events) }
    }

    override fun Route.createPass(): Route = post("/pass") {
        val data = mapper.readValue<CreatePassData>(call.request.receiveChannel().toByteArray().decodeToString())
        val pass = passService.createPass(data)
        call.respondText(ContentType.Application.Json, HttpStatusCode.OK) { mapper.writeValueAsString(pass) }
    }

    override fun Route.renewPass(): Route = post("/pass/{passId}") {
        val data = mapper.readValue<RenewPassData>(call.request.receiveChannel().toByteArray().decodeToString())
        val pass = passService.renew(call.parameters["passId"].orEmpty(), data)
        call.respondText(ContentType.Application.Json, HttpStatusCode.OK) { mapper.writeValueAsString(pass) }
    }
}