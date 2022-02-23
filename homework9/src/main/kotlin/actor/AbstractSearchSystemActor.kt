package actor

import akka.actor.UntypedAbstractActor
import com.fasterxml.jackson.databind.ObjectMapper
import domain.Link
import domain.message.ResultMessage
import domain.message.SearchMessage
import java.io.InputStream
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers

abstract class AbstractSearchSystemActor: UntypedAbstractActor() {
    protected companion object {
        val client = HttpClient.newHttpClient()!!
        const val resultSize = 5
    }

    abstract val url: String

    protected fun doRequest(message: Any?): String? {
        if (message !is SearchMessage) {
            sendResult()
            return null
        }
        val request = HttpRequest
            .newBuilder()
            .GET()
            .uri(URI(url.format(message.searchQuery, resultSize)))
            .build()
        val response = client.send(request, BodyHandlers.ofString())!!
        if (response.statusCode() != 200) {
            sendResult()
            return null
        }
        return response.body()
    }

    protected fun sendResult(links: List<Link> = emptyList()) {
        sender.tell(ResultMessage(links), self)
    }
}