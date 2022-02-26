package repository

import com.mongodb.client.model.Filters
import com.mongodb.rx.client.MongoDatabase
import domain.*
import domain.data.AddProductData
import org.bson.Document
import org.bson.types.ObjectId
import rx.Observable

class ProductRepository(
    mongoDatabase: MongoDatabase
) {
    private val collection = mongoDatabase.getCollection("products")

    fun save(data: AddProductData): Observable<Product> =
        Observable
            .just(data)
            .map {
                Document().apply {
                    append("_id", ObjectId.get().toHexString())
                    append("name", it.name)
                    append("price", it.price)
                }
            }
            .flatMap { doc ->
                collection.insertOne(doc).map { doc["_id"] }
            }
            .flatMap { get(it.toString()) }

    fun get(id: String): Observable<Product> =
        Observable.just(
            id
        )
            .flatMap {
                collection
                    .find(Filters.eq("_id", it))
                    .first()
            }
            .map { it.toProduct() }

    fun getAll(): Observable<Product> =
        collection
            .find()
            .toObservable()
            .map { it.toProduct() }

    private fun Document.toProduct() = Product(
        get("_id").toString(),
        get("name").toString(),
        get("price").toString().toDouble()
    )
}