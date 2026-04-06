package bitxon.cloud.springgcp.testcontainers

import com.github.dockerjava.api.model.ExposedPort
import com.github.dockerjava.api.model.HostConfig
import com.github.dockerjava.api.model.PortBinding
import com.github.dockerjava.api.model.Ports
import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.MountableFile
import java.net.ServerSocket

private const val DOCKER_IMAGE = "fsouza/fake-gcs-server"
private const val INIT_DATA_FOLDER = "/data"

class StorageEmulatorContainerExt : GenericContainer<StorageEmulatorContainerExt>(DOCKER_IMAGE) {

    private val port: Int = getRandomAvailablePort()

    init {
        withExposedPorts(port)
        withCreateContainerCmdModifier { cmd ->
            cmd.withEntrypoint("/bin/fake-gcs-server",
                "-scheme", "http",
                "-port", port.toString(),
                "-external-url", "http://localhost:${port}",
                "-data", INIT_DATA_FOLDER,
                "-backend", "memory"
            )
            cmd.withHostConfig(HostConfig().withPortBindings(PortBinding(
                Ports.Binding.bindPort(port),
                ExposedPort(port)
            )))
        }
    }

    fun withInitData(folderToCopy: MountableFile): StorageEmulatorContainerExt {
        return withCopyFileToContainer(folderToCopy, INIT_DATA_FOLDER)
    }

    val emulatorEndpoint: String
        get() = "http://${host}:${getMappedPort(port)}"

    private fun getRandomAvailablePort(): Int {
        return ServerSocket(0).use { it.localPort }
    }
}