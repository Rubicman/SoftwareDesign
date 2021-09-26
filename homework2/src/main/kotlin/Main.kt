import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

fun main(args: Array<String>) {
    val recordStatisticCollector = RecordStatisticCollectorImp(
        getVkApi("https://api.vk.com/method/"),
        System.getenv("accessToken"),
        System.getenv("apiVersion")
    )
    val results = recordStatisticCollector.getRecordCount("#выборы", 12)
    results.forEach { println(it) }
}

fun getVkApi(url: String): VkApi {
    val mapper = ObjectMapper()
    mapper.registerModule(KotlinModule())
    mapper.registerModule(JavaTimeModule())
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(JacksonConverterFactory.create(mapper))
        .build()
    return retrofit.create(VkApi::class.java)
}