import dto.VkResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface VkApi {
    @GET("newsfeed.search")
    fun searchRecord(
        @Query("q") searchQuery: String,
        @Query("access_token") accessToken: String,
        @Query("v") apiVersion: String,
        @Query("count") recordCount: Long? = null,
        @Query("latitude") latitude: Double? = null,
        @Query("longitude") longitude: Double? = null,
        @Query("start_time") startTime: Long? = null,
        @Query("end_time") endTime: Long? = null,
        @Query("start_from") startFrom: String? = null,
    ): Call<VkResponse>
}