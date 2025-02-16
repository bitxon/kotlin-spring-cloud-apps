package bitxon.cloud.springazure.testcontainers

import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.images.builder.Transferable
import org.testcontainers.utility.DockerImageName

private val DEFAULT_IMAGE: DockerImageName =
    DockerImageName.parse("mcr.microsoft.com/azure-messaging/eventhubs-emulator:latest")
const val CONNECTION_STRING_FORMAT =
    "Endpoint=sb://%s:%d;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=SAS_KEY_VALUE;UseDevelopmentEmulator=true;"
const val JAAS_CONFIG_FORMAT =
    "org.apache.kafka.common.security.plain.PlainLoginModule required username='\$ConnectionString' password='%s';"
const val AMQP_PORT = 5672
const val KAFKA_PORT = 9092

class EventHubContainer() : GenericContainer<EventHubContainer>(DEFAULT_IMAGE) {

    private var azuriteContainer: AzuriteContainer? = null

    init {
        waitingFor(Wait.forLogMessage(".*Emulator Service is Successfully Up!.*", 1))
        withExposedPorts(AMQP_PORT, KAFKA_PORT)
        withEnv("ACCEPT_EULA", "Y")
    }

    fun withConfig(config: Transferable): EventHubContainer {
        return withCopyToContainer(config, "/Eventhubs_Emulator/ConfigFiles/Config.json")
    }

    fun withAzuriteContainer(azuriteContainer: AzuriteContainer): EventHubContainer {
        this.azuriteContainer = azuriteContainer
        return dependsOn(azuriteContainer)
    }

    override fun configure() {
        super.configure()
        // https://github.com/Azure/azure-event-hubs-emulator-installer/blob/main/Docker-Compose-Template/docker-compose-custom-ports-linux.yml
        withExtraHost("host.docker.internal", "host-gateway")
        withEnv("BLOB_SERVER", "host.docker.internal:${azuriteContainer!!.blobPort}")
        withEnv("METADATA_SERVER", "host.docker.internal:${azuriteContainer!!.tablePort}")
    }

    val connectionString: String
        get() = CONNECTION_STRING_FORMAT.format(host, getMappedPort(AMQP_PORT))

    val bootstrapServers: String
        get() = "${host}:${getMappedPort(KAFKA_PORT)}"

    val jaasConfig: String
        get() = JAAS_CONFIG_FORMAT.format(connectionString)

}