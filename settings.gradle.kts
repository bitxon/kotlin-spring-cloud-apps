plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "kotlin-spring-clouds"

include(
    "spring-aws",
    "spring-azure",
    "spring-gcp"
)
