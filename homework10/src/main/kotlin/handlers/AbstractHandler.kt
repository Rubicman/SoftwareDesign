package handlers

import io.netty.buffer.ByteBuf
import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import io.reactivex.netty.protocol.http.server.HttpServerResponse
import io.reactivex.netty.protocol.http.server.RequestHandler
import rx.Observable

abstract class AbstractHandler: RequestHandler<ByteBuf, ByteBuf> {
    override fun handle(
        request: HttpServerRequest<ByteBuf>,
        response: HttpServerResponse<ByteBuf>,
    ): Observable<Void> {
        val content = request.content.autoRelease()
            .map { buf ->
                val bytes = ByteArray(buf.readableBytes())
                buf.readBytes(bytes)
                String(bytes)
            }
            .flatMap { handle(it) }
        return response
            .setStatus(HttpResponseStatus.OK)
            .setHeader("Content-Type", "application/json")
            .writeString(content)
    }

    abstract fun handle(json: String): Observable<String>
}