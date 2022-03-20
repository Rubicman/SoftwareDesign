import client.impl.ExchangeClientImpl
import com.fasterxml.jackson.databind.SerializationFeature
import controller.impl.ExceptionHandlerImpl
import controller.impl.PersonalAreaControllerImpl
import domain.Company
import domain.data.AddUserData
import domain.data.BuyOrSellData
import domain.data.ChangeBalanceData
import domain.dto.StocksDto
import domain.dto.SummaryBalanceDto
import domain.dto.UserDto
import domain.dto.UserIdDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.ContentType.Application
import io.ktor.http.HttpStatusCode.Companion.NoContent
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.testcontainers.containers.FixedHostPortGenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import repository.impl.InMemoryUserRepository
import service.impl.PersonalAreaServiceImpl
import kotlin.test.assertEquals

@Testcontainers
class IntegrationTest {

    /*@Container
    private val mongoContainer = FixedHostPortGenericContainer("mongo")
        .withFixedExposedPort(30000, 27017)
    private val mongoUrl by lazy { "mongodb://${mongoContainer.host}:30000" }*/

    @Container
    private val exchangeContainer = FixedHostPortGenericContainer("exchange")
        .withFixedExposedPort(8081, 8081)

    //.withEnv("MONGO_URL", mongoUrl)
    //.dependsOn(mongoContainer)
    private val exchangeUrl by lazy { "http://${exchangeContainer.host}:8081" }

    private val externalClient = HttpClient {
        expectSuccess = false
        install(JsonPlugin) {
            serializer = JacksonSerializer()
        }
    }

    @BeforeEach
    fun waitForStart() {
        Thread.sleep(1000)
    }

    @Test
    fun `success scenario test`() = testApplication {
        configureTestApplication()
        val internalClient = client.config {
            install(JsonPlugin) {
                expectSuccess = false
                serializer = JacksonSerializer()
            }
        }

        val company1Id = externalClient.createCompany("1", 100.0, 10)
        val company2Id = externalClient.createCompany("2", 50.0, 15)

        val user1Id = internalClient.createUser("1", 1000.0)
        val user2Id = internalClient.createUser("2", 1200.0)

        internalClient.changeBalance(user1Id, 500.0)
        assertEquals(1500.0, internalClient.getSummaryBalance(user1Id))

        internalClient.buyStocks(user2Id, company2Id, 8)
        assertEquals(1200.0, internalClient.getSummaryBalance(user2Id))
        assertEquals(UserDto(user2Id, "2", 800.0), internalClient.getUser(user2Id))
        assertEquals(Company("2", 7, 50.0, company2Id), externalClient.getCompany(company2Id))
        assertEquals(listOf(StocksDto("2", company2Id, 8, 50.0)), internalClient.getStocks(user2Id))

        externalClient.setPrice(company2Id, 70.0)
        assertEquals(1360.0, internalClient.getSummaryBalance(user2Id))
        assertEquals(listOf(StocksDto("2", company2Id, 8, 70.0)), internalClient.getStocks(user2Id))

        internalClient.sellStocks(user2Id, company2Id, 4)
        externalClient.setPrice(company2Id, 40.0)
        assertEquals(1240.0, internalClient.getSummaryBalance(user2Id))
        assertEquals(UserDto(user2Id, "2", 1080.0), internalClient.getUser(user2Id))
        assertEquals(Company("2", 11, 40.0, company2Id), externalClient.getCompany(company2Id))
        assertEquals(listOf(StocksDto("2", company2Id, 4, 40.0)), internalClient.getStocks(user2Id))

        internalClient.buyStocks(user2Id, company1Id, 5)
        assertEquals(1240.0, internalClient.getSummaryBalance(user2Id))
        assertEquals(UserDto(user2Id, "2", 580.0), internalClient.getUser(user2Id))
        assertEquals(Company("1", 5, 100.0, company1Id), externalClient.getCompany(company1Id))
        assertEquals(
            listOf(StocksDto("2", company2Id, 4, 40.0), StocksDto("1", company1Id, 5, 100.0)),
            internalClient.getStocks(user2Id))
    }

    private fun ApplicationTestBuilder.configureTestApplication() {
        val userRepository = InMemoryUserRepository()
        val exchangeClient = ExchangeClientImpl(exchangeUrl, externalClient)
        val personalAreaService = PersonalAreaServiceImpl(userRepository, exchangeClient)

        val exceptionHandler = ExceptionHandlerImpl()
        val personalAreaController = PersonalAreaControllerImpl(personalAreaService)

        application {
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
        }
    }

    private fun HttpClient.createCompany(name: String, startStockPrice: Double, stocks: Int = 0) = runBlocking {
        post("$exchangeUrl/company") {
            contentType(Application.Json)
            setBody(mapOf(
                "name" to name,
                "startStockPrice" to startStockPrice,
                "stocks" to stocks
            ))
        }.let {
            assertEquals(OK, it.status)
            it.body<Map<String, String>>()["id"]!!
        }
    }

    private fun HttpClient.createUser(name: String, startBalance: Double) = runBlocking {
        post("/user") {
            contentType(Application.Json)
            setBody(AddUserData(name, startBalance))
        }.let {
            assertEquals(OK, it.status)
            it.body<UserIdDto>().id
        }
    }

    private fun HttpClient.changeBalance(userId: String, amount: Double) = runBlocking {
        post("/user/$userId/balance") {
            contentType(Application.Json)
            setBody(ChangeBalanceData(amount))
        }.let { assertEquals(NoContent, it.status) }
    }

    private fun HttpClient.getSummaryBalance(userId: String) = runBlocking {
        get("/user/$userId/balance")
            .let {
                assertEquals(OK, it.status)
                it.body<SummaryBalanceDto>().summaryBalance
            }
    }

    private fun HttpClient.getUser(userId: String) = runBlocking {
        get("/user/$userId")
            .let {
                assertEquals(OK, it.status)
                it.body<UserDto>()
            }
    }

    private fun HttpClient.buyStocks(userId: String, companyId: String, amount: Int) = runBlocking {
        post("/user/$userId/buy") {
            contentType(Application.Json)
            setBody(BuyOrSellData(companyId, amount))
        }.let { assertEquals(NoContent, it.status) }
    }

    private fun HttpClient.sellStocks(userId: String, companyId: String, amount: Int) = runBlocking {
        post("/user/$userId/sell") {
            contentType(Application.Json)
            setBody(BuyOrSellData(companyId, amount))
        }.let { assertEquals(NoContent, it.status) }
    }

    private fun HttpClient.getStocks(userId: String) = runBlocking {
        get("/user/$userId/stocks")
            .let {
                assertEquals(OK, it.status)
                it.body<List<StocksDto>>()
            }
    }

    private fun HttpClient.getCompany(companyId: String) = runBlocking {
        get("$exchangeUrl/company/$companyId")
            .let {
                assertEquals(OK, it.status)
                it.body<Company>()
            }
    }

    private fun HttpClient.setPrice(companyId: String, newPrice: Double) = runBlocking {
        post("$exchangeUrl/company/$companyId/price") {
            contentType(Application.Json)
            setBody(mapOf("newPrice" to newPrice))
        }
            .let {
                assertEquals(NoContent, it.status)
            }
    }
}