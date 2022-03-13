package manager

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mongodb.client.MongoClients
import common.repository.impl.EnterRepositoryImpl
import common.repository.impl.PassRepositoryImpl
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import manager.controller.impl.PassControllerImpl
import manager.service.impl.EnterServiceImpl
import manager.service.impl.PassServiceImpl
import java.time.Clock
import java.time.ZoneId

fun main() {
    val mapper = jacksonObjectMapper()
        .registerModule(JavaTimeModule())
    val client = MongoClients.create()
    val database = client.getDatabase("homework11")

    val passRepository = PassRepositoryImpl(database, mapper)
    val enterRepository = EnterRepositoryImpl(database, mapper)

    val passService = PassServiceImpl(passRepository, Clock.system(ZoneId.of("UTC+03:00")))
    val enterService = EnterServiceImpl(enterRepository)

    val passController = PassControllerImpl(passService, enterService, mapper)
    embeddedServer(Netty, port = 8001) {
        routing {
            passController.apply {
                getEvents()
                createPass()
                renewPass()
            }
        }
    }.start(wait = true)
}