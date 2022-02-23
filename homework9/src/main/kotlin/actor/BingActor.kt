package actor

import com.fasterxml.jackson.databind.ObjectMapper
import domain.Link
import domain.SearchSystem.*

class BingActor : AbstractSearchSystemActor() {
    override val url = "${System.getenv("BING_BASE_URL")}/api-search?searchQuery=%s&size=%s"

    override fun onReceive(message: Any?) {
        val response = doRequest(message) ?: return
        val links = ObjectMapper()
            .readTree(response)
            .flatten()
            .map { it["link"] }
            .map { it.textValue() }
            .map { Link(BING, it) }
        sendResult(links)
    }
}