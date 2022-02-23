package actor

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import domain.Link
import domain.SearchSystem.*

class YandexActor : AbstractSearchSystemActor() {

    override val url = "${System.getenv("YANDEX_BASE_URL")}/api/search?query=%s"

    override fun onReceive(message: Any?) {
        val response = doRequest(message) ?: return
        val links = XmlMapper()
            .readTree(response)
            .flatten()
            .map { Link(YANDEX, it.asText()) }
            .take(resultSize)
        sendResult(links)
    }
}