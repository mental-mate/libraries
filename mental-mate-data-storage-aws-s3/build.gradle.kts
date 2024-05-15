extra["projectDescription"] = "AWS S3 based data storage implementation"

val awsSdkVersion: String by rootProject.extra

dependencies {
    api(project(":mental-mate-data-storage"))
    api(project(":mental-mate-util"))

    api("aws.sdk.kotlin:s3:$awsSdkVersion")
}
