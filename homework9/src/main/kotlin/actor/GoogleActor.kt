package actor

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import domain.Link
import domain.SearchSystem.*
import java.net.URI

class GoogleActor : AbstractSearchSystemActor() {
    override val url = "${System.getenv("GOOGLE_BASE_URL")}/api/v3/search?q=%s&n=%s&simple=true"

    override fun onReceive(message: Any?) {
        val response = doRequest(message) ?: return
        val links = ObjectMapper()
            .readTree(response)
            .map { it.textValue() }
            .map { Link(GOOGLE, it) }
        sendResult(links)
    }
}