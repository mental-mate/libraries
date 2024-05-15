package tech.harmonysoft.oss.mentalmate.storage.data.aws.s3

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.DeleteObjectRequest
import aws.sdk.kotlin.services.s3.model.ListObjectsRequest
import aws.sdk.kotlin.services.s3.model.PutObjectRequest
import aws.smithy.kotlin.runtime.content.ByteStream
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.springframework.stereotype.Component
import tech.harmonysoft.oss.mentalmate.storage.data.DataStorage
import tech.harmonysoft.oss.mentalmate.storage.data.DataStorageDir
import tech.harmonysoft.oss.mentalmate.storage.data.DataStorageFile
import tech.harmonysoft.oss.mentalmate.storage.data.aws.s3.config.AwsS3ConfigProvider
import tech.harmonysoft.oss.mentalmate.storage.data.aws.s3.model.AwsS3File
import tech.harmonysoft.oss.mentalmate.util.config.ConfigurationHelper

@Component
class AwsS3DataStorage(
    private val config: AwsS3ConfigProvider,
    private val logger: Logger,
    configHelper: ConfigurationHelper
) : DataStorage<AwsS3File> {

    init {
        configHelper.verifyPropertyIsSet("AWS_REGION")
        configHelper.verifyPropertyIsSet("AWS_ACCESS_KEY_ID")
        configHelper.verifyPropertyIsSet("AWS_SECRET_ACCESS_KEY")
    }

    override fun listFiles(dir: DataStorageDir): Collection<AwsS3File> = runBlocking {
        val bucketName = config.data.bucket
        S3Client.fromEnvironment().use { s3 ->
            s3.listObjects(ListObjectsRequest {
                bucket = bucketName
                prefix = normaliseDirPath(dir.path)
            }).contents?.mapNotNull { s3Object ->
                s3Object.takeIf { it.key?.endsWith("/") == false }?.let { s3File ->
                    s3File.key?.let { fileKey ->
                        AwsS3File(
                            bucketName = bucketName,
                            name = fileKey
                        )
                    } ?: run {
                        logger.error("can not find key in AWS S3 file from bucket {}: {}", bucketName, s3File)
                        null
                    }
                }
            } ?: emptyList()
        }.also {
            logger.info(
                "found {} files in AWS S3 directory {} of bucket {}: {}",
                it.size, dir.path, bucketName, it.joinToString { f -> f.name }
            )
        }
    }

    override fun getDir(path: String): DataStorageDir {
        return DataStorageDir(path)
    }

    override fun createFile(dir: DataStorageDir, name: String, content: ByteArray): AwsS3File {
        val bucketName = config.data.bucket
        val fileName = normaliseDirPath(dir.path) + name
        runBlocking {
            S3Client.fromEnvironment().use { s3 ->
                s3.putObject(PutObjectRequest {
                    bucket = bucketName
                    key = fileName
                    body = ByteStream.fromBytes(content)
                })
            }
        }
        logger.info("created AWS S3 file '{}' in bucket {}", fileName, bucketName)
        return AwsS3File(
            bucketName = bucketName,
            name = fileName
        )
    }

    private fun normaliseDirPath(path: String): String {
        return if (path.endsWith("/")) {
            path
        } else {
            "$path/"
        }
    }

    override fun delete(file: AwsS3File) {
        val bucketName = config.data.bucket
        runBlocking {
            S3Client.fromEnvironment().use { s3 ->
                s3.deleteObject(DeleteObjectRequest {
                    bucket = bucketName
                    key = file.name
                })
            }
        }
        logger.info("successfully processed AWS S3 removal request for file '{}' in bucket {}", file.name, bucketName)
    }
}