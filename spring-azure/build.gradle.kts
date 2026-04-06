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

extra["springCloudAzureVersion"] = "7.1.0"

dependencyManagement {
    imports {
        mavenBom("com.azure.spring:spring-cloud-azure-dependencies:${property("springCloudAzureVersion")}")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

//    implementation("com.azure.spring:spring-cloud-azure-starter-actuator")
    implementation("com.azure.spring:spring-cloud-azure-starter-data-cosmos")
    implementation("com.azure.spring:spring-cloud-azure-starter-storage")
    implementation("com.azure.spring:spring-cloud-azure-starter-integration-eventhubs") // ??? Remove ???
    implementation("org.springframework.boot:spring-boot-starter-kafka")


    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("com.azure.spring:spring-cloud-azure-testcontainers")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.testcontainers:testcontainers-junit-jupiter")
    testImplementation("org.testcontainers:testcontainers-azure")
    testImplementation("io.rest-assured:rest-assured:6.0.0")
    testImplementation("org.assertj:assertj-core")
    testImplementation("org.awaitility:awaitility")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
