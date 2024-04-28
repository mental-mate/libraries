val harmonysoftLibsVersion by extra { "3.1.0" }

subprojects {
    extra["projectType"] = "library"
    extra["projectUrl"] = "https://github.com/mental-mate/libraries.git"

    version = "2.0.0"

    dependencies {
        api("tech.harmonysoft:harmonysoft-slf4j-spring:$harmonysoftLibsVersion")
        api("tech.harmonysoft:harmonysoft-default-implementations:$harmonysoftLibsVersion")
    }
}