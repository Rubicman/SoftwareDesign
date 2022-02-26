package service

import domain.data.AddProductData
import domain.Currency
import domain.Product
import domain.ProductWithCurrency
import repository.ProductRepository
import rx.Observable

class ProductService(
    private val userService: UserService,
    private val productRepository: ProductRepository,
    private val currencyService: CurrencyService,
) {

    fun getAll(userId: String): Observable<List<ProductWithCurrency>> =
        userService
            .get(userId)
            .flatMap { user ->
                productRepository
                    .getAll()
                    .map { product -> product.withCurrency(user.currency) }
            }
            .collect<MutableList<ProductWithCurrency>>(
                { mutableListOf() },
                { list, product -> list.add(product) }
            )
            .map { it.toList() }

    fun add(data: AddProductData): Observable<ProductWithCurrency> =
        userService
            .get(data.userId)
            .map { user ->
                user.currency to data.copy(price = currencyService.convert(data.price, user.currency, Currency.RUB))
            }
            .flatMap { (currency, data) ->
                productRepository.save(data).map { it.withCurrency(currency) }
            }

    private fun Product.withCurrency(currency: Currency) =
        ProductWithCurrency(
            id,
            name,
            currencyService.convert(
                rubPrice,
                Currency.RUB,
                currency),
            currency
        )
}