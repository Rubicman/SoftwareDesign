package client

import domain.Company

interface ExchangeClient {
    fun changeStocks(companyId: String, value: Int)
    fun getInfo(companyId: String): Company?
}