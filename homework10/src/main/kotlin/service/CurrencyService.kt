package service

import domain.Currency

class CurrencyService(
    private val usdPrice: () -> Double,
    private val eurPrice: () -> Double,
) {
    fun convert(price: Double, from: Currency, to: Currency): Double {
        val fromPrice = when (from) {
            Currency.RUB -> price
            Currency.USD -> fromUsd(price)
            Currency.EUR -> fromEur(price)
        }

        return when (to) {
            Currency.RUB -> fromPrice
            Currency.USD -> toUsd(fromPrice)
            Currency.EUR -> toEur(fromPrice)
        }
    }

    fun fromUsd(usdProductPrice: Double) = usdProductPrice * usdPrice()

    fun toUsd(rubProductPrice: Double) = rubProductPrice / usdPrice()

    fun fromEur(eurProductPrice: Double) = eurProductPrice * eurPrice()

    fun toEur(rubProductPrice: Double) = rubProductPrice / eurPrice()
}