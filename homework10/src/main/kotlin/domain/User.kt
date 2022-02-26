package domain

import domain.dto.UserViewDto

data class User(
    val id: String,
    val name: String,
    val currency: Currency
) {
    fun toViewDto() = UserViewDto(
        id = id,
        name = name,
        currency = currency
    )
}
