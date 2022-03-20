package servis.impl

import domain.Company
import domain.data.AddCompanyData
import domain.data.ChangeStocksData
import domain.data.SetPriceData
import domain.exception.NoSuchCompanyException
import domain.exception.NotEnoughStocksException
import repository.CompanyRepository
import service.ExchangeService

class ExchangeServiceImpl(
    private val companyRepository: CompanyRepository,
): ExchangeService {
    override fun addCompany(addCompanyData: AddCompanyData): String =
        companyRepository.create(Company(
            name = addCompanyData.name,
            stocks = addCompanyData.stocks,
            price = addCompanyData.startStockPrice
        )).id


    override fun getInfo(id: String): Company? =
        companyRepository.get(id)

    override fun changeStock(id: String, data: ChangeStocksData) {
        val company = companyRepository.get(id) ?: throw NoSuchCompanyException(id)
        if (company.stocks + data.count < 0) throw NotEnoughStocksException(company, data.count)
        companyRepository.save(company.copy(stocks = company.stocks + data.count))
    }

    override fun setPrice(id: String, data: SetPriceData) {
        val company = companyRepository.get(id) ?: throw NoSuchCompanyException(id)
        if (data.newPrice < 0) throw IllegalArgumentException("Price can't be negative")
        companyRepository.save(company.copy(price = data.newPrice))
    }

}