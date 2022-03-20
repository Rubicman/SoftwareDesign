package repository

import domain.Company

interface CompanyRepository {
    fun get(id: String): Company?
    fun create(company: Company): Company
    fun save(company: Company)
}
