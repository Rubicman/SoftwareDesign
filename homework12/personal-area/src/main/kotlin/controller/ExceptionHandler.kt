package controller

import io.ktor.server.plugins.*

interface ExceptionHandler {
    fun StatusPagesConfig.noSuchUser()
    fun StatusPagesConfig.notEnoughAmount()
    fun StatusPagesConfig.illegalArgument()
    fun StatusPagesConfig.internalsErrors()
}