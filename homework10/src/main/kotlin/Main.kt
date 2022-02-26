import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mongodb.rx.client.MongoClients
import handlers.AddProductHandler
import handlers.GetProductsHandler
import handlers.UserRegistrationHandler
import io.netty.handler.codec.http.HttpMethod
import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.netty.protocol.http.server.HttpServer
import repository.ProductRepository
import repository.UserRepository
import service.CurrencyService
import service.ProductService
import service.UserService

fun main() {
    val mongoClient = MongoClients.create()
    val mongoDatabase = mongoClient.getDatabase("sd_homework10")
    val mapper = jacksonObjectMapper()

    val userRepository = UserRepository(mongoDatabase)
    val productRepository = ProductRepository(mongoDatabase)

    val currencyService = CurrencyService({ 83.55 }, { 93.60 })
    val userService = UserService(userRepository)
    val productService = ProductService(userService, productRepository, currencyService)

    val addProductHandler = AddProductHandler(productService, mapper)
    val getProductsHandler = GetProductsHandler(productService, mapper)
    val userRegistrationHandler = UserRegistrationHandler(userService, mapper)

    HttpServer
        .newServer(8080)
        .start { request, response ->
            when {
                "/registration" == request.decodedPath && HttpMethod.POST == request.httpMethod ->
                    userRegistrationHandler.handle(request, response)
                "/products/add" == request.decodedPath && HttpMethod.POST == request.httpMethod ->
                    addProductHandler.handle(request, response)
                "/products" == request.decodedPath && HttpMethod.GET == request.httpMethod ->
                    getProductsHandler.handle(request, response)
                else -> response
                    .setStatus(HttpResponseStatus.BAD_REQUEST)
            }
        }
        .awaitShutdown()
}