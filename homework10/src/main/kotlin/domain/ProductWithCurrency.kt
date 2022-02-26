package domain

import domain.dto.ProductViewDto

data class ProductWithCurrency(
    val id: String,
    val name: String,
    val price: Double,
    val currency: Currency
) {
    fun toViewDto() =
        ProductViewDto(
            id,
            name,
            price,
            currency.toString()
        )
}