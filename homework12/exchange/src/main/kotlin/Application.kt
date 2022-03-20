import com.fasterxml.jackson.databind.SerializationFeature
import controller.impl.ExchangeControllerImpl
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.*
import io.ktor.server.routing.*
import repository.impl.InMemoryCompanyRepository
import servis.impl.ExchangeServiceImpl

fun main() {
    /*val mongoUrl = System.getenv("MONGO_URL") ?: "mongodb://localhost"
    val client = MongoClients.create(mongoUrl)
    val pojoCodecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
        CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).
        build()))
    val database = client.getDatabase("homework12").withCodecRegistry(pojoCodecRegistry)

    val companyRepository = CompanyRepositoryImpl(database)*/
    val companyRepository = InMemoryCompanyRepository()
    val exchangeService = ExchangeServiceImpl(companyRepository)
    val exchangeController = ExchangeControllerImpl(exchangeService)

    embeddedServer(Netty, port = 8081, host = "0.0.0.0") {
        install(ContentNegotiation) {
            jackson {
                enable(SerializationFeature.INDENT_OUTPUT)
            }
        }
        routing {
            exchangeController.apply {
                addCompany()
                getInfo()
                changeStocks()
                setPrice()
            }
        }
    }.start(wait = true)
}
