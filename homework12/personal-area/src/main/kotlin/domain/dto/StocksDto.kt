package domain.dto

data class StocksDto(
    val companyName: String,
    val companyId: String,
    val amount: Int,
    val currentPrice: Double,
)