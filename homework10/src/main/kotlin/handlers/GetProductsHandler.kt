package handlers

import com.fasterxml.jackson.databind.ObjectMapper
import io.netty.buffer.ByteBuf
import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import io.reactivex.netty.protocol.http.server.HttpServerResponse
import io.reactivex.netty.protocol.http.server.RequestHandler
import rx.Observable
import service.ProductService

class GetProductsHandler(
    private val productService: ProductService,
    private val mapper: ObjectMapper,
) : RequestHandler<ByteBuf, ByteBuf> {
    override fun handle(
        request: HttpServerRequest<ByteBuf>,
        response: HttpServerResponse<ByteBuf>,
    ): Observable<Void> {
        val content = Observable
            .just(request)
            .map { it.queryParameters["userId"].orEmpty().firstOrNull() }
            .flatMap { productService.getAll(it!!) }
            .map { list -> list.map { it.toViewDto() } }
            .map { mapper.writeValueAsString(it) }
        return response
            .setStatus(HttpResponseStatus.OK)
            .setHeader("Content-Type", "application/json")
            .writeString(content)
    }
}