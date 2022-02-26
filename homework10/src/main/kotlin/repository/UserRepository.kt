package repository

import com.mongodb.client.model.Filters.eq
import com.mongodb.rx.client.MongoDatabase
import domain.User
import domain.data.UserRegistrationData
import org.bson.Document
import org.bson.types.ObjectId
import rx.Observable
import domain.Currency

class UserRepository(
    mongoDatabase: MongoDatabase,
) {
    private val collection = mongoDatabase.getCollection("users")

    fun save(data: UserRegistrationData): Observable<User> =
        Observable
            .just(data)
            .map {
                Document().apply {
                    append("_id", ObjectId.get().toHexString())
                    append("name", it.name)
                    append("currency", it.currency)
                }
            }
            .flatMap { doc ->
                collection.insertOne(doc).map { doc["_id"] }
            }
            .flatMap { get(it.toString()) }

    fun get(id: String): Observable<User> =
        Observable.just(
            id
        )
            .flatMap {
                collection
                    .find(eq("_id", it))
                    .first()
            }
            .map {
                User(
                    it["_id"].toString(),
                    it["name"].toString(),
                    Currency.valueOf(it["currency"].toString())
                )
            }
}