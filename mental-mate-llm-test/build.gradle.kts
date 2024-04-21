extra["projectDescription"] = "LLM test library"

val harmonysoftLibsVersion: String by rootProject.extra

dependencies {
    api(project(":mental-mate-llm"))

    api("tech.harmonysoft:harmonysoft-common-test:$harmonysoftLibsVersion")
}
