package bitxon.cloud.springgcp

import com.google.api.gax.core.CredentialsProvider
import com.google.api.gax.core.NoCredentialsProvider
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.context.DynamicPropertyRegistrar
import org.testcontainers.containers.FirestoreEmulatorContainer
import org.testcontainers.utility.DockerImageName


@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfig {

    @Bean
    fun propertiesOverride(firestoreContainer: FirestoreEmulatorContainer): DynamicPropertyRegistrar {
        return DynamicPropertyRegistrar { registry ->
            registry.add("spring.cloud.gcp.firestore.host-port") { firestoreContainer.emulatorEndpoint }
            registry.add("spring.cloud.gcp.firestore.emulator.enabled") { "true" } // TODO why this is not working? Why i have to specify it in Yaml?
        }
    }


    @Bean
    fun googleCredentials(): CredentialsProvider = NoCredentialsProvider.create()

    @Bean
    fun firestoreContainer() =
        FirestoreEmulatorContainer(DockerImageName.parse("gcr.io/google.com/cloudsdktool/google-cloud-cli:emulators"))
}
