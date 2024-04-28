val harmonysoftLibsVersion by extra { "3.0.0" }

subprojects {
    extra["projectType"] = "library"
    extra["projectUrl"] = "https://github.com/mental-mate/libraries.git"

    version = "2.0.0"

    dependencies {
        api("tech.harmonysoft:inpertio-client-kotlin-spring:1.5.0")
        api("tech.harmonysoft:harmonysoft-slf4j-spring:$harmonysoftLibsVersion")
        api("tech.harmonysoft:harmonysoft-default-implementations:$harmonysoftLibsVersion")
    }
}