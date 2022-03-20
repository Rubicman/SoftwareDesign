package domain

import domain.dto.StocksDto

data class Stocks(
    val companyName: String,
    val companyId: String,
    val amount: Int,
    val currentPrice: Double,
) {
    val dto
        get() = StocksDto(
            companyName = companyName,
            companyId = companyId,
            amount = amount,
            currentPrice = currentPrice
        )
}
