package tech.harmonysoft.oss.mentalmate.storage.data.aws.s3.config.impl

import org.springframework.stereotype.Component
import tech.harmonysoft.oss.configurario.client.DelegatingConfigProvider
import tech.harmonysoft.oss.configurario.client.factory.ConfigProviderFactory
import tech.harmonysoft.oss.mentalmate.storage.data.aws.s3.config.AwsS3Config
import tech.harmonysoft.oss.mentalmate.storage.data.aws.s3.config.AwsS3ConfigProvider

data class RawAwsS3Config(
    val bucket: String
)

@Component
class AwsS3ConfigProviderImpl(
    factory: ConfigProviderFactory
) : DelegatingConfigProvider<AwsS3Config>(

    factory.build(RawAwsS3Config::class.java, "aws") { raw ->
        if (raw.bucket.isBlank()) {
            throw IllegalStateException("mandatory property 'bucket' can't be blank")
        }
        AwsS3Config(raw.bucket)
    }
), AwsS3ConfigProvider