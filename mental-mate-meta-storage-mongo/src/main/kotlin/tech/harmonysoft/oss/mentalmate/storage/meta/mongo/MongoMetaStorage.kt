package tech.harmonysoft.oss.mentalmate.storage.meta.mongo

import com.mongodb.DuplicateKeyException
import java.time.Duration
import java.time.Instant
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.slf4j.Logger
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.findOne
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Component
import tech.harmonysoft.oss.common.ProcessingResult
import tech.harmonysoft.oss.common.time.clock.ClockProvider
import tech.harmonysoft.oss.mentalmate.storage.meta.MetaStorage

@Component
class MongoMetaStorage(
    private val mongoTemplate: ReactiveMongoTemplate,
    private val clockProvider: ClockProvider,
    private val logger: Logger
) : MetaStorage {

    override suspend fun getValue(key: String): String? {
        val now = Instant.now(clockProvider.data)
        return mongoTemplate.findOne<MetaDocument>(
            query(
                Criteria(Property.KEY).isEqualTo(key)
                    .and(Property.EXPIRATION_TIME).gt(now)
            )
        ).awaitFirstOrNull()
            ?.value
            .also {
                logger.info("found the following meta value for key '{}': {}", key, it)
            }
    }

    override suspend fun storeIfNotSet(key: String, value: String, ttlMs: Long): ProcessingResult<Unit, String> {
        val now = Instant.now(clockProvider.data)
        return try {
            mongoTemplate.insert(MetaDocument(key, value, now + Duration.ofMillis(ttlMs)))
            logger.info("stored value '{}' for a meta key '{}' with ttl of {} ms", value, key, ttlMs)
            ProcessingResult.success()
        } catch (e: DuplicateKeyException) {
            val existing = getValue(key)
            logger.info(
                "can not store value '{}' for a meta key '{}' with ttl of {} ms - expected that "
                + "no value is set for that key now but it has '{}'",
                value, key, ttlMs, existing
            )
            ProcessingResult.failure(existing.toString())
        }
    }

    override suspend fun compareAndSet(
        key: String,
        expectedValue: String,
        newValue: String,
        ttlMs: Long
    ): ProcessingResult<Unit, String> {
        val now = Instant.now(clockProvider.data)
        val result = mongoTemplate.updateFirst(
            query(
                Criteria(Property.KEY).isEqualTo(key)
                    .and(Property.VALUE).isEqualTo(expectedValue)
            ),
            Update.update(Property.VALUE, newValue)
                .set(Property.VALUE, now + Duration.ofMillis(ttlMs)),
            MetaDocument::class.java
        ).awaitFirstOrNull()

        return if (result == null) {
            val existing = getValue(key)
            logger.info(
                "can not store value '{}' for a meta key '{}' with ttl of {} ms - expected that it currently "
                + "has value '{}' but it has value '{}'",
                newValue, key, ttlMs, expectedValue, existing
            )
            ProcessingResult.failure(existing.toString())
        } else {
            ProcessingResult.success()
        }
    }

    object Property {
        val KEY = MetaDocument::key.name
        val VALUE = MetaDocument::value.name
        val EXPIRATION_TIME = MetaDocument::expirationTime.name
    }
}