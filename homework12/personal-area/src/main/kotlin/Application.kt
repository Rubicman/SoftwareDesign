import client.impl.ExchangeClientImpl
import com.fasterxml.jackson.databind.SerializationFeature
import controller.impl.ExceptionHandlerImpl
import controller.impl.PersonalAreaControllerImpl
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.json.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.*
import io.ktor.server.routing.*
import repository.impl.InMemoryUserRepository
import service.impl.PersonalAreaServiceImpl

fun main() {
    val exchangeUrl = System.getenv("EXCHANGE_URL") ?: "http://localhost:8081"

    val httpClient = HttpClient(CIO) {
        expectSuccess = false
        install(JsonPlugin) {
            serializer = JacksonSerializer()
        }
    }
    val userRepository = InMemoryUserRepository()
    val exchangeClient = ExchangeClientImpl(exchangeUrl, httpClient)
    val personalAreaService = PersonalAreaServiceImpl(userRepository, exchangeClient)

    val exceptionHandler = ExceptionHandlerImpl()
    val personalAreaController = PersonalAreaControllerImpl(personalAreaService)

    embeddedServer(Netty, port = 8082, host = "0.0.0.0") {
        install(ContentNegotiation) {
            jackson {
                enable(SerializationFeature.INDENT_OUTPUT)
            }
        }
        install(StatusPages) {
            exceptionHandler.apply {
                noSuchUser()
                notEnoughAmount()
                internalsErrors()
            }
        }
        routing {
            personalAreaController.apply {
                addUser()
                changeBalance()
                getUser()
                getUserStocks()
                getUserSummaryBalance()
                buyStocks()
                sellStocks()
            }
        }
    }.start(wait = true)
}
