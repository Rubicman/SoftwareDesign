package controller

import io.ktor.server.routing.*

interface PersonalAreaController {
    fun Route.addUser(): Route
    fun Route.changeBalance(): Route
    fun Route.getUser(): Route
    fun Route.getUserStocks(): Route
    fun Route.getUserSummaryBalance(): Route
    fun Route.buyStocks(): Route
    fun Route.sellStocks(): Route
}