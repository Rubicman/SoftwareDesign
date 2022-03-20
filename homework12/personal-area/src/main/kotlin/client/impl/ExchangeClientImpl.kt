package client.impl

import client.ExchangeClient
import domain.Company
import domain.dto.ChangeStocksDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.ContentType.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.NoContent
import io.ktor.http.HttpStatusCode.Companion.NotFound
import io.ktor.http.HttpStatusCode.Companion.OK
import kotlinx.coroutines.runBlocking

class ExchangeClientImpl(
    private val exchangeUrl: String,
    private val client: HttpClient,
) : ExchangeClient {
    override fun changeStocks(companyId: String, value: Int) = runBlocking {
        val response = client.post("$exchangeUrl/company/$companyId/stocks") {
            contentType(Application.Json)
            setBody(ChangeStocksDto(value))
        }
        when (response.status) {
            NotFound -> throw IllegalArgumentException("No company with id '$companyId'")
            BadRequest -> throw IllegalArgumentException("Not enough stocks of company with id $companyId on exchange")
            NoContent -> {}
            else -> throw RuntimeException(
                "Unknown status code ${response.status} for /company/$companyId/stocks with body '${response.body<String>()}'"
            )
        }
    }

    override fun getInfo(companyId: String): Company? = runBlocking {
        val response = client.get("$exchangeUrl/company/$companyId")
        when (response.status) {
            OK -> response.body<Company>()
            NotFound -> null
            else -> throw RuntimeException(
                "Unknown status code ${response.status} for /company/$companyId with body '${response.body<String>()}'"
            )
        }
    }
}