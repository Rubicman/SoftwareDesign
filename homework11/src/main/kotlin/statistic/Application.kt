package statistic

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mongodb.client.MongoClients
import common.repository.impl.EnterRepositoryImpl
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import statistic.controller.impl.StatisticControllerImpl
import statistic.service.impl.StatisticServiceImpl
import java.time.Clock
import java.time.ZoneId
import java.time.ZonedDateTime

fun main() {
    val mapper = jacksonObjectMapper()
        .registerModule(JavaTimeModule())
    val client = MongoClients.create()
    val database = client.getDatabase("homework11")

    val enterRepository = EnterRepositoryImpl(database, mapper)

    val statisticService = StatisticServiceImpl(
        enterRepository,
        ZonedDateTime.parse("2022-03-07T10:00:00+03:00"),
        Clock.system(ZoneId.of("UTC+03:00"))
    )

    val statisticController = StatisticControllerImpl(statisticService, mapper)

    embeddedServer(Netty, port = 8003) {
        routing {
            statisticController.apply {
                byDay()
                averages()
            }
        }
    }.start(wait = true)
}