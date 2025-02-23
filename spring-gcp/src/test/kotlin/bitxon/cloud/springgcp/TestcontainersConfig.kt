package bitxon.cloud.springgcp

import bitxon.cloud.springgcp.testcontainers.PubSubEmulatorContainerExt
import bitxon.cloud.springgcp.testcontainers.StorageEmulatorContainer
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.context.DynamicPropertyRegistrar
import org.testcontainers.containers.FirestoreEmulatorContainer
import org.testcontainers.containers.PubSubEmulatorContainer
import org.testcontainers.utility.DockerImageName
import org.testcontainers.utility.MountableFile


@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfig {

    @Bean
    fun propertiesOverride(
        firestoreContainer: FirestoreEmulatorContainer,
        pubSubContainer: PubSubEmulatorContainer,
        storageContainer: StorageEmulatorContainer
    ): DynamicPropertyRegistrar {
        return DynamicPropertyRegistrar { registry ->
            // Common
            registry.add("spring.cloud.gcp.development-mode") { true }
            // Firestore
            registry.add("spring.cloud.gcp.firestore.host-port") { firestoreContainer.emulatorEndpoint }
            registry.add("spring.cloud.gcp.firestore.emulator.enabled") { "true" } // TODO why this is not working? Why i have to specify it in Yaml?
            // Pub/Sub
            registry.add("spring.cloud.gcp.pubsub.emulator-host") { pubSubContainer.emulatorEndpoint }
            // Storage
            registry.add("spring.cloud.gcp.storage.host") { storageContainer.emulatorEndpoint }
        }
    }

    @Bean
    fun firestoreContainer() =
        FirestoreEmulatorContainer(DockerImageName.parse("gcr.io/google.com/cloudsdktool/google-cloud-cli:emulators"))

    @Bean
    fun storageContainer() = StorageEmulatorContainer()
        .withInitData(MountableFile.forClasspathResource("data"))

    @Bean
    fun pubSubContainer() =
        PubSubEmulatorContainerExt(DockerImageName.parse("gcr.io/google.com/cloudsdktool/google-cloud-cli:emulators"))

}
