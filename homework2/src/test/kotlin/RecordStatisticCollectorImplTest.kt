import dto.VkRecord
import dto.VkRecordSearchResult
import dto.VkResponse
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.mockito.kotlin.*
import org.testng.annotations.Test
import retrofit2.Call
import retrofit2.Response
import java.time.ZonedDateTime
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertFails

class RecordStatisticCollectorImplTest {

    @Test
    fun singleTest() {
        val vkMock: VkApi = mock()
        val call = getMockCall(isSuccess = true, hasNext = false, totalCount = 10, count = 10)
        whenever(
            vkMock.searchRecord(
                eq("New Year"),
                eq("qwerty"),
                eq("1.0"),
                eq(null),
                eq(null),
                eq(null),
                any(),
                eq(null),
                eq(null)
            )
        ).thenReturn(call)
        val collector = RecordStatisticCollectorImp(vkMock, "qwerty", "1.0")
        val results = collector.getRecordCount("New Year", 12)
        assertEquals(10L, results.sum())
        verify(vkMock).searchRecord(
            any(),
            any(),
            any(),
            anyOrNull(),
            anyOrNull(),
            anyOrNull(),
            anyOrNull(),
            anyOrNull(),
            anyOrNull()
        )
    }

    @Test
    fun manyTest() {
        val vkMock: VkApi = mock()
        val call1 = getMockCall(isSuccess = true, hasNext = true, totalCount = 140, count = 30)
        val call2 = getMockCall(isSuccess = true, hasNext = true, totalCount = 140, count = 30)
        val call3 = getMockCall(isSuccess = true, hasNext = true, totalCount = 140, count = 30)
        val call4 = getMockCall(isSuccess = true, hasNext = true, totalCount = 140, count = 30)
        val call5 = getMockCall(isSuccess = true, hasNext = false, totalCount = 140, count = 20)
        val call6 = getMockCall(isSuccess = true, hasNext = true, totalCount = 140, count = 30)
        whenever(
            vkMock.searchRecord(
                eq("New Year"),
                eq("qwerty"),
                eq("1.0"),
                eq(null),
                eq(null),
                eq(null),
                any(),
                eq(null),
                anyOrNull()
            )
        ).thenReturn(call1).thenReturn(call2).thenReturn(call3).thenReturn(call4).thenReturn(call5).thenReturn(call6)
        val collector = RecordStatisticCollectorImp(vkMock, "qwerty", "1.0")
        val results = collector.getRecordCount("New Year", 12)
        assertEquals(140L, results.sum())
        verify(vkMock, times(5)).searchRecord(
            any(),
            any(),
            any(),
            anyOrNull(),
            anyOrNull(),
            anyOrNull(),
            anyOrNull(),
            anyOrNull(),
            anyOrNull()
        )
    }

    @Test
    fun exceptionTest() {
        val vkMock: VkApi = mock()
        val call = getMockCall(isSuccess = false, hasNext = false, totalCount = 10, count = 10)
        whenever(
            vkMock.searchRecord(
                eq("New Year"),
                eq("qwerty"),
                eq("1.0"),
                eq(null),
                eq(null),
                eq(null),
                any(),
                eq(null),
                eq(null)
            )
        ).thenReturn(call)
        val collector = RecordStatisticCollectorImp(vkMock, "qwerty", "1.0")
        assertFails {
            collector.getRecordCount("New Year", 12)
        }
    }

    private fun getMockCall(isSuccess: Boolean, hasNext: Boolean, totalCount: Long, count: Long): Call<VkResponse> {
        val response: Response<VkResponse> =
            if (isSuccess) Response.success(getRandomVkResponse(hasNext, totalCount, count))
            else Response.error(400, ResponseBody.create(MediaType.get("application/json"), "{}"))
        val call: Call<VkResponse> = mock()
        whenever(call.execute()).thenReturn(response)
        return call
    }

    private fun getRandomVkResponse(hasNext: Boolean, totalCount: Long, count: Long): VkResponse {
        val random = Random(System.currentTimeMillis())
        return VkResponse(
            VkRecordSearchResult(
                count,
                totalCount,
                if (hasNext) getRandomString() else null,
                List(count.toInt()) {
                    VkRecord(
                        random.nextLong(),
                        ZonedDateTime.now().minusHours(random.nextLong(12))
                    )
                }
            )
        )
    }

    private fun getRandomString(): String {
        val characters = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return List(10) { characters.random() }.joinToString()
    }
}