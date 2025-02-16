package bitxon.cloud.springazure

import bitxon.cloud.springazure.testcontainers.AzuriteContainer
import bitxon.cloud.springazure.testcontainers.EventHubContainer
import bitxon.cloud.springazure.testcontainers.CosmosDbContainer
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.test.context.DynamicPropertyRegistrar
import org.testcontainers.utility.MountableFile

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfig {

    @Bean
    fun propertiesOverride(cosmosDb: CosmosDbContainer, eventHub: EventHubContainer): DynamicPropertyRegistrar {
        return DynamicPropertyRegistrar { registry ->
            registry.add("spring.cloud.azure.cosmos.endpoint") { cosmosDb.emulatorEndpoint }
            registry.add("spring.cloud.azure.cosmos.key") { cosmosDb.emulatorKey }
            registry.add("spring.cloud.azure.cosmos.connection-mode") { "gateway" }

            registry.add("spring.kafka.bootstrap-servers") { eventHub.bootstrapServers }
            registry.add("spring.kafka.consumer.bootstrap-servers") { eventHub.bootstrapServers }
            registry.add("spring.kafka.properties.sasl.jaas.config") { eventHub.jaasConfig }
            registry.add("spring.kafka.properties.sasl.mechanism") { "PLAIN" }
            registry.add("spring.kafka.properties.security.protocol") { "SASL_PLAINTEXT" }
        }
    }

    @Bean
    @ServiceConnection(name = "azure-storage/azurite")
    fun azuriteContainer() = AzuriteContainer()

    @Bean
    fun cosmosDbContainer() = CosmosDbContainer()

    @Bean
    fun eventHubsContainer(azuriteContainer: AzuriteContainer) = EventHubContainer()
        .withConfig(MountableFile.forClasspathResource("configs/eventhub.json"))
        .withAzuriteContainer(azuriteContainer)

}
