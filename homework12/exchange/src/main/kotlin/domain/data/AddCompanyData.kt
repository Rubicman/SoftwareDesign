package domain.data

data class AddCompanyData(
    val name: String,
    val startStockPrice: Double,
    val stocks: Int = 0,
)