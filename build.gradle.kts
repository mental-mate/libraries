val harmonysoftLibsVersion by extra { "3.2.0" }
val awsSdkVersion by extra { "1.2.7" }

subprojects {
    extra["projectType"] = "library"
    extra["projectUrl"] = "https://github.com/mental-mate/libraries.git"

    version = "2.8.0"

    dependencies {
        api("tech.harmonysoft:harmonysoft-slf4j-spring:$harmonysoftLibsVersion")
        api("tech.harmonysoft:harmonysoft-default-implementations:$harmonysoftLibsVersion")
    }
}