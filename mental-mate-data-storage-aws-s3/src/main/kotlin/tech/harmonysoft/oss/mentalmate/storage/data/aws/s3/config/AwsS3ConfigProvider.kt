package tech.harmonysoft.oss.mentalmate.storage.data.aws.s3.config

import tech.harmonysoft.oss.configurario.client.ConfigProvider

data class AwsS3Config(
    val bucket: String
)

interface AwsS3ConfigProvider : ConfigProvider<AwsS3Config>