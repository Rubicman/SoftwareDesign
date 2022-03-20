package domain.dbo

import domain.Company
import org.bson.types.ObjectId

data class CompanyDbo(
    var name: String,
    var stocks: Int,
    var price: Double,
    var _id: ObjectId = ObjectId.get()
) {
    constructor() : this("", 0, 0.0)

    fun toModel(id: String) = Company(
        name = name,
        stocks = stocks,
        price = price,
        id = id
    )
}
