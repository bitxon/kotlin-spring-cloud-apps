package bitxon.cloud.springazure

import bitxon.cloud.springazure.testcontainers.CosmosDBEmulatorVNextContainer
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.test.context.DynamicPropertyRegistrar
import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName
import java.lang.String.format

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfig {

    @Bean
    fun propertiesOverride(
        cosmosDbContainer: CosmosDBEmulatorVNextContainer,
        azuriteContainer: GenericContainer<*>
    ): DynamicPropertyRegistrar {
        return DynamicPropertyRegistrar { registry ->
            registry.add("spring.cloud.azure.cosmos.endpoint") { cosmosDbContainer.emulatorEndpoint}
            registry.add("spring.cloud.azure.cosmos.key") { cosmosDbContainer.emulatorKey}
            registry.add("spring.cloud.azure.cosmos.connection-mode") { "gateway"}
            //registry.add("spring.cloud.azure.storage.connection-string") { getConnectionString(azuriteContainer) }
        }
    }

    @Bean
    @ServiceConnection(name = "azure-storage/azurite")
    fun azuriteContainer(): GenericContainer<*> {
        return GenericContainer(DockerImageName.parse("mcr.microsoft.com/azure-storage/azurite:latest"))
            .withExposedPorts(10000, 10001, 10002)
    }

    @Bean
    fun cosmosDbContainer(): CosmosDBEmulatorVNextContainer {
        return CosmosDBEmulatorVNextContainer()
    }

    private fun getAzuriteConnectionString(container: GenericContainer<*>): String {
        return format(
            "DefaultEndpointsProtocol=http;AccountName=devstoreaccount1;AccountKey=Eby8vdM02xNOcqFlqUwJPLlmEtlCDXJ1OUzFT50uSRZ6IFsuFq2UVErCz4I6tq/K1SZFPTOtr/KBHBeksoGMGw==;BlobEndpoint=http://%s:%d/devstoreaccount1;QueueEndpoint=http://%s:%d/devstoreaccount1;TableEndpoint=http://%s:%d/devstoreaccount1;",
            container.host, container.getMappedPort(10000),
            container.host, container.getMappedPort(10001),
            container.host, container.getMappedPort(10002),
        )
    }

}
