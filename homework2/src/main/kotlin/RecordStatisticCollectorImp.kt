import dto.VkRecord
import retrofit2.HttpException
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.logging.SimpleFormatter

class RecordStatisticCollectorImp(
    private val vkApi: VkApi,
    private val accessToken: String,
    private val apiVersion: String
) : RecordStatisticCollector {
    override fun getRecordCount(hashtag: String, lookBackHours: Long): List<Long> {
        val startTime = ZonedDateTime.now()
            .minusHours(lookBackHours)
            .toInstant()
            .epochSecond
        val records = mutableListOf<VkRecord>()
        var startFrom: String? = null
        do {
            val response = vkApi.searchRecord(
                searchQuery = hashtag,
                accessToken = accessToken,
                apiVersion = apiVersion,
                startTime = startTime,
                startFrom = startFrom)
                    .execute()
            if (!response.isSuccessful) {
                throw HttpException(response)
            }
            val searchResult = response.body()!!.response ?: throw HttpException(response)
            startFrom = searchResult.nextFrom
            records.addAll(searchResult.items)
        } while (startFrom != null)
        return records
            .groupBy {
                DateTimeFormatter.ofPattern("yyyy-MM-dd-hh").format(it.date)
            }
            .entries
            .sortedBy { it.key }
            .map { it.value.size.toLong() }
    }
}