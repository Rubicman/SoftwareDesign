import com.pyruby.stubserver.StubMethod
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import com.pyruby.stubserver.StubServer
import dto.VkResponse
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.fail

private const val PORT = 34567

class VkApiTest {

    @Test(dataProvider = "vkApiTestData")
    fun successTest(vkApi: VkApi, server: StubServer) = try {
        server
            .expect(StubMethod.get("/method/newsfeed.search?q=New%20Year&access_token=qwerty&v=1.0.0"))
            .thenReturn(
                200,
                "application/json",
                "{\"response\":{\"count\":1,\"total_count\": 5,\"next_from\":\"123\",\"items\":[]}}"
            )

        val result = vkApi.searchRecord("New Year", "qwerty", "1.0.0").execute().body()

        server.verify()
        assertNotEquals(null, result)
        result as VkResponse
        assertEquals(5, result.response!!.totalCount)
    } catch (e: RuntimeException) {
        fail("Exception can't be thrown")
    } finally {
        server.stop()
    }

    @Test(dataProvider = "vkApiTestData")
    fun failTest(vkApi: VkApi, server: StubServer) = try {
        server
            .expect(StubMethod.get("/method/newsfeed.search?q=New%20Year&access_token=qwerty&v=1.0.0"))
            .thenReturn(404)
        
        val result = vkApi.searchRecord("New Year", "qwerty", "1.0.0").execute()

        server.verify()
        assertFalse(result.isSuccessful)
    } catch (e: RuntimeException) {
        fail("Exception can't be thrown")
    } finally {
        server.stop()
    }

    @DataProvider
    fun vkApiTestData(): Array<Array<Any>> {
        val vkApi = getVkApi("http://localhost:$PORT/method/")
        val server = StubServer(PORT)
        server.start()
        return arrayOf(arrayOf(vkApi, server))
    }
}