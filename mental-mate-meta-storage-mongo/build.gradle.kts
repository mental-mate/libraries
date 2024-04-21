extra["projectDescription"] = "mongo metadata storage implementation"

dependencies {
    api(project(":mental-mate-meta-storage"))

    api("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
}