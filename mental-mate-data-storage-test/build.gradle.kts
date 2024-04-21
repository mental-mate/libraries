extra["projectDescription"] = "utility library to facilitate testing of the 'storage' library"

val harmonysoftLibsVersion: String by rootProject.extra

dependencies {
    api(project(":mental-mate-data-storage"))

    api("tech.harmonysoft:harmonysoft-common-cucumber:$harmonysoftLibsVersion")
}
