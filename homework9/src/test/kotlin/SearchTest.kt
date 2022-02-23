import actor.MainActor
import akka.actor.*
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import domain.Link
import domain.SearchSystem.*
import domain.message.ResultMessage
import domain.message.SearchMessage
import org.testng.annotations.AfterTest
import org.testng.annotations.BeforeTest
import org.testng.annotations.Test
import java.util.concurrent.CompletableFuture

import kotlin.test.assertEquals
import kotlin.test.assertNull

class SearchTest {
    private lateinit var server: WireMockServer
    private lateinit var system: ActorSystem
    private lateinit var mainActor: ActorRef

    @BeforeTest
    fun beforeTest() {
        server = WireMockServer(80)
        system = ActorSystem.create()
        mainActor = system.actorOf(Props.create(MainActor::class.java))
        server.start()
    }

    @AfterTest
    fun stopServer() {
        server.stop()
    }

    @Test
    fun `results from all search systems`() {
        server.stubFor(
            get(urlEqualTo(bingUrl))
                .willReturn(aResponse().withStatus(200).withBody(bingResponse))
        )
        server.stubFor(
            get(urlEqualTo(googleUrl))
                .willReturn(aResponse().withStatus(200).withBody(googleResponse))
        )
        server.stubFor(
            get(urlEqualTo(yandexUrl))
                .willReturn(aResponse().withStatus(200).withBody(yandexResponse))
        )

        val result = search("testQuery").groupBy(Link::searchSystem)

        assertEquals(bingLinks, result[BING])
        assertEquals(googleLinks, result[GOOGLE])
        assertEquals(yandexLinks, result[YANDEX])
    }

    @Test
    fun `Not success status code on some search system`() {
        server.stubFor(
            get(urlEqualTo(bingUrl))
                .willReturn(aResponse().withStatus(500))
        )
        server.stubFor(
            get(urlEqualTo(googleUrl))
                .willReturn(aResponse().withStatus(200).withBody(googleResponse))
        )
        server.stubFor(
            get(urlEqualTo(yandexUrl))
                .willReturn(aResponse().withStatus(301).withBody(yandexResponse))
        )

        val result = search("testQuery").groupBy(Link::searchSystem)

        assertNull(result[BING])
        assertEquals(googleLinks, result[GOOGLE])
        assertNull(result[YANDEX])
    }

    @Test
    fun `Timeout on some search system`() {
        server.stubFor(
            get(urlEqualTo(bingUrl))
                .willReturn(aResponse().withStatus(500).withFixedDelay(5000))
        )
        server.stubFor(
            get(urlEqualTo(googleUrl))
                .willReturn(aResponse().withStatus(200).withBody(googleResponse).withFixedDelay(3500))
        )
        server.stubFor(
            get(urlEqualTo(yandexUrl))
                .willReturn(aResponse().withStatus(200).withBody(yandexResponse))
        )

        val result = search("testQuery").groupBy(Link::searchSystem)

        assertNull(result[BING])
        assertNull(result[GOOGLE])
        assertEquals(yandexLinks, result[YANDEX])
    }

    private fun search(query: String): List<Link> {
        val future = CompletableFuture<List<Link>>()
        val testActor = system.actorOf(Props.create(TestActor::class.java, future))
        mainActor.tell(SearchMessage(query), testActor)
        future.join()
        return future.get()
    }

    class TestActor(private val future: CompletableFuture<List<Link>>) :
        UntypedAbstractActor() {

        override fun onReceive(message: Any?) {
            if (message !is ResultMessage) {
                return
            }
            future.complete(message.links)
        }
    }

    companion object {
        const val bingUrl = "/bing/api-search?searchQuery=testQuery&size=5"
        const val bingResponse = "{\n" +
                "  \"result\": [\n" +
                "    {\n" +
                "      \"link\": \"https://bing-link-1\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"link\": \"https://bing-link-2\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"link\": \"https://bing-link-3\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"link\": \"https://bing-link-4\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"link\": \"https://bing-link-5\"\n" +
                "    }\n" +
                "  ]\n" +
                "}"
        val bingLinks = listOf(
            Link(BING, "https://bing-link-1"),
            Link(BING, "https://bing-link-2"),
            Link(BING, "https://bing-link-3"),
            Link(BING, "https://bing-link-4"),
            Link(BING, "https://bing-link-5")
        )
        const val googleUrl = "/google/api/v3/search?q=testQuery&n=5&simple=true"
        const val googleResponse = "[\n" +
                "  \"https://google-link-1\",\n" +
                "  \"https://google-link-2\",\n" +
                "  \"https://google-link-3\",\n" +
                "  \"https://google-link-4\",\n" +
                "  \"https://google-link-5\"\n" +
                "]"
        val googleLinks = listOf(
            Link(GOOGLE, "https://google-link-1"),
            Link(GOOGLE, "https://google-link-2"),
            Link(GOOGLE, "https://google-link-3"),
            Link(GOOGLE, "https://google-link-4"),
            Link(GOOGLE, "https://google-link-5")
        )
        const val yandexUrl = "/yandex/api/search?query=testQuery"
        const val yandexResponse = "<result>\n" +
                "    <link>https://yandex-link-1</link>\n" +
                "    <link>https://yandex-link-2</link>\n" +
                "    <link>https://yandex-link-3</link>\n" +
                "    <link>https://yandex-link-4</link>\n" +
                "    <link>https://yandex-link-5</link>\n" +
                "    <link>https://yandex-link-6</link>\n" +
                "    <link>https://yandex-link-7</link>\n" +
                "    <link>https://yandex-link-8</link>\n" +
                "    <link>https://yandex-link-9</link>\n" +
                "    <link>https://yandex-link-10</link>\n" +
                "</result>"
        val yandexLinks = listOf(
            Link(YANDEX, "https://yandex-link-1"),
            Link(YANDEX, "https://yandex-link-2"),
            Link(YANDEX, "https://yandex-link-3"),
            Link(YANDEX, "https://yandex-link-4"),
            Link(YANDEX, "https://yandex-link-5")
        )
    }
}