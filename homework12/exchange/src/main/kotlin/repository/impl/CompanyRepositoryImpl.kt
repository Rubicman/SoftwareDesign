package repository.impl

import domain.Company
import domain.dbo.CompanyDbo
import repository.CompanyRepository
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters.eq
import org.bson.types.ObjectId

class CompanyRepositoryImpl(
    database: MongoDatabase,
) : CompanyRepository {
    private val collection = database.getCollection("companies", CompanyDbo::class.java)

    override fun get(id: String): Company? =
        collection.find(eq("_id", ObjectId(id))).firstOrNull()?.toModel(id)

    override fun create(company: Company): Company {
        val id = collection.insertOne(company.toDbo())
            .insertedId?.asObjectId()?.value?.toHexString() ?: ""
        return company.copy(id = id)
    }

    override fun save(company: Company) {
        company.toDbo().let {
            collection.replaceOne(eq("_id", it._id), it)
        }
    }
}