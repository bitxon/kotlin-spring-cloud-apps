package bitxon.cloud.springazure.testcontainers

import com.github.dockerjava.api.command.InspectContainerResponse
import com.github.dockerjava.api.model.ExposedPort
import com.github.dockerjava.api.model.HostConfig
import com.github.dockerjava.api.model.PortBinding
import com.github.dockerjava.api.model.Ports
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName
import java.net.ServerSocket
import java.nio.file.Files
import java.security.KeyStore
import kotlin.io.path.Path
import kotlin.io.path.outputStream

private val DEFAULT_IMAGE: DockerImageName =
    DockerImageName.parse("mcr.microsoft.com/cosmosdb/linux/azure-cosmos-emulator:vnext-preview")

class CosmosDbContainer() : GenericContainer<CosmosDbContainer>(DEFAULT_IMAGE) {

    private val port: Int = getRandomAvailablePort()
    private var protocol: String = "https"

    init {
        withExposedPorts(port)
        withEnv("PORT", port.toString())
        withEnv("PROTOCOL", protocol)
        withEnv("ENABLE_EXPLORER", "false")
        withEnv("LOG_LEVEL", "trace")
        withCreateContainerCmdModifier { cmd ->
            cmd.withHostConfig(HostConfig().withPortBindings(PortBinding(Ports.Binding.bindPort(port), ExposedPort(port))))
        }
        waitingFor(Wait.forLogMessage(".*Now listening on.*\\n", 1))
    }

    fun withProtocol(protocol: String): CosmosDbContainer {
        this.protocol = protocol
        return withEnv("PROTOCOL", protocol)
    }

    override fun containerIsStarted(containerInfo: InspectContainerResponse?) {
        super.containerIsStarted(containerInfo)

        when(protocol) {
            "http" -> {
                System.setProperty("COSMOS.HTTP_CONNECTION_WITHOUT_TLS_ALLOWED", "true")
            }
            "https" -> {
                val tempFolderPath = Files.createTempDirectory("certs")
                val keyStoreFilePath = Path(tempFolderPath.toString(), "azure-cosmos-emulator.keystore")
                val keyStore = this.buildNewKeyStore()
                keyStore.store(keyStoreFilePath.outputStream(), emulatorKey.toCharArray())

                System.setProperty("javax.net.ssl.trustStore", keyStoreFilePath.toString())
                System.setProperty("javax.net.ssl.trustStorePassword", emulatorKey)
                System.setProperty("javax.net.ssl.trustStoreType", "PKCS12")
            }
        }
    }

    fun buildNewKeyStore(): KeyStore {
        return buildByExtractingCertificate(emulatorKey)
    }

    val emulatorKey: String
        get() = "C2y6yDjf5/R+ob0N8A7Cgv30VRDJIWEHLM+4QDU5DE2nQ9nDuVTqobD4b8mGGyPMbIZnqyMsEcaGQy67XIw/Jw=="

    val emulatorEndpoint: String
        get() = "${protocol}://${this.host}:${getMappedPort(port)}"

    private fun getRandomAvailablePort(): Int {
        return ServerSocket(0).use { it.localPort }
    }
}