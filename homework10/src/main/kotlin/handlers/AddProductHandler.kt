package handlers

import com.fasterxml.jackson.databind.ObjectMapper
import domain.data.AddProductData
import rx.Observable
import service.ProductService

class AddProductHandler(
    private val productService: ProductService,
    private val mapper: ObjectMapper,
) : AbstractHandler() {
    override fun handle(json: String): Observable<String> =
        Observable
            .just(json)
            .map { mapper.readValue(it, AddProductData::class.java) }
            .flatMap { productService.add(it) }
            .map { it.toViewDto() }
            .map { mapper.writeValueAsString(it) }

}