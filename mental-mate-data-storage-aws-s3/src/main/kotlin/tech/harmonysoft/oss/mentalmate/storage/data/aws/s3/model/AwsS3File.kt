package tech.harmonysoft.oss.mentalmate.storage.data.aws.s3.model

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.GetObjectRequest
import aws.smithy.kotlin.runtime.content.toByteArray
import java.io.InputStream
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.slf4j.LoggerFactory
import tech.harmonysoft.oss.mentalmate.storage.data.DataStorageDir
import tech.harmonysoft.oss.mentalmate.storage.data.DataStorageFile

data class AwsS3File(
    val bucketName: String,
    override val name: String
) : DataStorageFile {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override val parent = run {
        val i = name.lastIndexOf("/")
        if (i <= 0) {
            DataStorageDir("")
        } else {
            DataStorageDir(name.substring(0, i))
        }
    }

    override fun getContent(): InputStream = runBlocking {
        val deferred = CompletableDeferred<ByteArray>()
        S3Client.fromEnvironment().use { s3 ->
            s3.getObject(GetObjectRequest {
                bucket = bucketName
                key = name
            }) {
                it.body?.let { bytes ->
                    deferred.complete(bytes.toByteArray())
                } ?: run {
                    logger.error("can not get content for AWS S3 file {} from bucket {}", name, bucketName)
                }
            }
        }
        withTimeout(1000) {
            deferred.await().inputStream()
        }
    }
}