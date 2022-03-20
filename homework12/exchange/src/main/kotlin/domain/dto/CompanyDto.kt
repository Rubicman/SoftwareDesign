package domain.dto

data class CompanyDto (
    val id: String,
    val name: String,
    val stocks: Int,
    val price: Double,
)