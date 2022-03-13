package common.repository.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.mongodb.BasicDBObject
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.eq
import common.domain.event.PassEvent
import common.domain.event.PassEventType
import common.repository.PassRepository
import org.bson.Document
import org.bson.conversions.Bson
import java.util.*

class PassRepositoryImpl(
    database: MongoDatabase,
    private val mapper: ObjectMapper,
) : PassRepository {
    private val collection = database.getCollection("passEvents")
    override fun get(passId: String?, type: PassEventType?): List<PassEvent> {
        val filters = buildList<Bson> {
            if (passId != null) add(eq("passId", passId))
            if (type != null) add(eq("type", type.toString()))
        }
        return collection.find(and(filters))
            .map { mapper.readValue<PassEvent>(it.toJson()) }
            .toList()
    }

    override fun latest(passId: String?): PassEvent? {
        return collection.find(eq("passId", passId))
            .sort(BasicDBObject("time", -1))
            .limit(1)
            .map { mapper.readValue<PassEvent>(it.toJson()) }
            .firstOrNull()
    }

    override fun save(event: PassEvent): PassEvent {
        collection.insertOne(Document.parse(mapper.writeValueAsString(event)))
        return event
    }

    override val nextUUID: UUID
        get() = UUID.randomUUID()
}