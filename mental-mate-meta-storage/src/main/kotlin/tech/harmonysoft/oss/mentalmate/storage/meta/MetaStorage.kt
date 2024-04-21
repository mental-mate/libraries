package tech.harmonysoft.oss.mentalmate.storage.meta

import tech.harmonysoft.oss.common.ProcessingResult
import tech.harmonysoft.oss.common.time.util.TimeUtil.Millis

interface MetaStorage {

    suspend fun getValue(key: String): String?

    suspend fun storeIfNotSet(key: String, value: Any, ttlMs: Long = Millis.DAY): ProcessingResult<Unit, String> {
        return storeIfNotSet(
            key = key,
            value = value.toString(),
            ttlMs = ttlMs
        )
    }

    /**
     * Stores given value for the given key if no value is set currently.
     *
     * @return  `Unit` in case of success; currently stored value otherwise (the storage is not updated in this case)
     */
    suspend fun storeIfNotSet(key: String, value: String, ttlMs: Long = Millis.DAY): ProcessingResult<Unit, String>

    suspend fun compareAndSet(
        key: String,
        expectedValue: Any,
        newValue: Any,
        ttlMs: Long = Millis.DAY
    ): ProcessingResult<Unit, String> {
        return compareAndSet(
            key = key,
            expectedValue = expectedValue.toString(),
            newValue = newValue.toString(),
            ttlMs = ttlMs
        )
    }

    /**
     * Stores given value for the given key if it currently has the expected value
     *
     * @return  `Unit` in case of success; currently stored value otherwise (the storage is not updated in this case)
     */
    suspend fun compareAndSet(
        key: String,
        expectedValue: String,
        newValue: String,
        ttlMs: Long = Millis.DAY
    ): ProcessingResult<Unit, String>
}