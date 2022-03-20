package service

import domain.Company
import domain.data.AddCompanyData
import domain.data.ChangeStocksData
import domain.data.SetPriceData

interface ExchangeService {
    fun addCompany(addCompanyData: AddCompanyData): String
    fun getInfo(id: String): Company?
    fun changeStock(id: String, data: ChangeStocksData)
    fun setPrice(id: String, data: SetPriceData)
}