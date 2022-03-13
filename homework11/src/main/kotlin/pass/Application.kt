package pass

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mongodb.client.MongoClients
import common.repository.impl.EnterRepositoryImpl
import common.repository.impl.PassRepositoryImpl
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import pass.controller.impl.EnterControllerImpl
import pass.service.impl.EnterServiceImpl
import java.time.Clock
import java.time.ZoneId

fun main() {
    val mapper = jacksonObjectMapper()
        .registerModule(JavaTimeModule())
    val client = MongoClients.create()
    val database = client.getDatabase("homework11")

    val passRepository = PassRepositoryImpl(database, mapper)
    val enterRepository = EnterRepositoryImpl(database, mapper)

    val enterService = EnterServiceImpl(enterRepository, passRepository, Clock.system(ZoneId.of("UTC+03:00")))

    val enterController = EnterControllerImpl(enterService)
    embeddedServer(Netty, port = 8002) {
        routing {
            enterController.apply {
                enter()
                exit()
            }
        }
    }.start(wait = true)
}