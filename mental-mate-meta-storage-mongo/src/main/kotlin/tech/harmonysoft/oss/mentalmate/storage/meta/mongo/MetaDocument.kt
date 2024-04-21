package tech.harmonysoft.oss.mentalmate.storage.meta.mongo

import java.time.Instant
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document("meta")
data class MetaDocument(
    @Indexed(unique = true) val key: String,
    val value: String,
    val expirationTime: Instant,
    @Id val id: ObjectId = ObjectId()
)