package statistic.controller.impl

import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import statistic.controller.StatisticController
import statistic.domain.dto.AverageStatisticDto
import statistic.domain.dto.DayStatisticDto
import statistic.service.StatisticService

class StatisticControllerImpl(
    private val statisticService: StatisticService,
    private val mapper: ObjectMapper,
) : StatisticController {
    override fun Route.byDay(): Route = get("/statistic/by-day") {
        call.respondText(
            ContentType.Application.Json,
            HttpStatusCode.OK
        ) {
            mapper.writeValueAsString(statisticService.byDay().map { (date, count) -> DayStatisticDto(date, count) })
        }
    }

    override fun Route.averages(): Route = get("/statistic/average") {
        call.respondText(
            ContentType.Application.Json,
            HttpStatusCode.OK
        ) {
            mapper.writeValueAsString(statisticService.averages().run { AverageStatisticDto(first, second) })
        }
    }
}