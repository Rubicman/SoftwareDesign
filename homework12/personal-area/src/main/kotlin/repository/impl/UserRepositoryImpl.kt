package repository.impl

import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters.eq
import domain.User
import domain.dbo.UserDbo
import org.bson.types.ObjectId
import repository.UserRepository

class UserRepositoryImpl(
    database: MongoDatabase,
) : UserRepository {
    private val collection = database.getCollection("users", UserDbo::class.java)

    override fun get(id: String): User? =
        collection.find(eq("_id", ObjectId(id))).firstOrNull()?.toModel(id)

    override fun create(user: User): User {
        val id = collection.insertOne(user.dbo)
            .insertedId?.asObjectId()?.value?.toHexString() ?: ""
        return user.copy(id = id)
    }

    override fun save(user: User) {
        user.dbo.let {
            collection.replaceOne(eq("_id", it._id), it)
        }
    }
}