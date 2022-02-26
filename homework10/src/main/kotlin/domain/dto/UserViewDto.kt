package domain.dto

import domain.Currency

data class UserViewDto(
    val id: String,
    val name: String,
    val currency: Currency
)