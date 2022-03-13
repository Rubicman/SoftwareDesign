package common.repository.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters.*
import common.domain.event.EnterEvent
import common.repository.EnterRepository
import org.bson.Document
import org.bson.conversions.Bson
import java.time.ZonedDateTime
import java.util.*

class EnterRepositoryImpl(
    database: MongoDatabase,
    private val mapper: ObjectMapper,
) : EnterRepository {
    private val collection = database.getCollection("enterEvents")
    override fun get(passId: String?, from: ZonedDateTime?): List<EnterEvent> {
        val filters = buildList<Bson> {
            if (passId != null) add(eq("passId", passId))
            if (from != null) add(gt("time", mapper.writeValueAsString(from).toDouble()))
        }
        val results = if (filters.isEmpty()) {
            collection.find()
        } else {
            collection.find(and(filters))
        }
        return results
            .map { mapper.readValue<EnterEvent>(it.toJson()) }
            .toList()
    }

    override fun save(event: EnterEvent): EnterEvent {
        collection.insertOne(Document.parse(mapper.writeValueAsString(event)))
        return event
    }

    override val nextUUID: UUID
        get() = UUID.randomUUID()

}