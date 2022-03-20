package controller

import io.ktor.server.routing.*

interface ExchangeController {
    fun Route.addCompany(): Route
    fun Route.changeStocks(): Route
    fun Route.getInfo(): Route
    fun Route.setPrice(): Route
}