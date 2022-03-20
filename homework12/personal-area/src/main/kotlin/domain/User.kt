package domain

import domain.dbo.UserDbo
import domain.dto.UserDto
import org.bson.types.ObjectId

data class User(
    val id: String = "",
    val name: String,
    var balance: Double,
    val stocks: MutableMap<String, Int> = mutableMapOf(),
) {
    val dbo
        get() = UserDbo(
            _id = if (id.isEmpty()) ObjectId.get() else ObjectId(id),
            name = name,
            balance = balance,
            stocks = stocks
        )

    val dto
        get() = UserDto(
            id = id,
            name = name,
            balance = balance,
        )
}
