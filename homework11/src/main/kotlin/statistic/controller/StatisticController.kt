package statistic.controller

import io.ktor.server.routing.*

interface StatisticController {
    fun Route.byDay(): Route
    fun Route.averages(): Route
}