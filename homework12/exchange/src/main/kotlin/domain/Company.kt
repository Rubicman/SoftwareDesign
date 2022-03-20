package domain

import domain.dbo.CompanyDbo
import domain.dto.CompanyDto
import org.bson.types.ObjectId

data class Company(
    val name: String,
    val stocks: Int,
    val price: Double,
    val id: String = "",
) {

    fun toDto() = CompanyDto(
        id = id,
        name = name,
        stocks = stocks,
        price = price
    )

    fun toDbo() = CompanyDbo(
        name = name,
        stocks = stocks,
        price = price,
        _id = if (id.isEmpty()) ObjectId.get() else ObjectId(id)
    )
}
