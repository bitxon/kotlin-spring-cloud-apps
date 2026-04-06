plugins {
    kotlin("jvm") version "2.3.20"
    kotlin("plugin.spring") version "2.3.20"
    id("org.springframework.boot") version "4.0.3"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "bitxon.cloud"
version = "0.0.1-SNAPSHOT"

java {
     toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}
kotlin {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xjsr305=strict", // treat JSR-305 nullability annotations as strict
            "-Xannotation-default-target=param-property" // apply annotations to both param and property (needed for Spring Data, Jackson reflection)
        )
    }
}

repositories {
    mavenCentral()
}

extra["springCloudGcpVersion"] = "8.0.1"
extra["springCloudVersion"] = "2025.1.1"

dependencyManagement {
    imports {
        mavenBom("com.google.cloud:spring-cloud-gcp-dependencies:${property("springCloudGcpVersion")}")
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("tools.jackson.module:jackson-module-kotlin")
    implementation("com.google.cloud:spring-cloud-gcp-starter-data-firestore")
    implementation("com.google.cloud:spring-cloud-gcp-starter-pubsub")
    implementation("com.google.cloud:spring-cloud-gcp-starter-storage")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.testcontainers:testcontainers-junit-jupiter")
    testImplementation("org.testcontainers:testcontainers-gcloud")
    testImplementation("io.rest-assured:rest-assured:6.0.0")
    testImplementation("org.assertj:assertj-core")
    testImplementation("org.awaitility:awaitility")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}


tasks.withType<Test> {
    useJUnitPlatform()
}
