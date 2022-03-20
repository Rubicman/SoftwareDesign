package domain.dbo

import domain.User
import org.bson.types.ObjectId

data class UserDbo(
    var _id: ObjectId,
    var name: String,
    var balance: Double,
    var stocks: MutableMap<String, Int>,
) {
    constructor(): this(ObjectId.get(), "", 0.0, mutableMapOf())

    fun toModel(id: String) = User(
        id = id,
        name = name,
        balance = balance,
        stocks = stocks
    )
}
