package repository.impl

import domain.Company
import repository.CompanyRepository
import java.util.concurrent.atomic.AtomicLong

class InMemoryCompanyRepository: CompanyRepository {
    private val nextId = AtomicLong(1)
    private val memory = mutableMapOf<String, Company>()

    override fun get(id: String): Company? =
        memory[id]

    override fun create(company: Company): Company {
        val id = nextId.getAndIncrement().toString()
        return company.copy(id = id).also {
            memory[id] = it
        }
    }

    override fun save(company: Company) {
        memory[company.id] = company
    }
}