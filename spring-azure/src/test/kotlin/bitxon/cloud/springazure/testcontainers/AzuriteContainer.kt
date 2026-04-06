package bitxon.cloud.springazure.testcontainers

import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName
import java.lang.String.format

private val DEFAULT_IMAGE: DockerImageName = DockerImageName.parse("mcr.microsoft.com/azure-storage/azurite:latest")
private const val BLOB_PORT = 10000
private const val QUEUE_PORT = 10001
private const val TABLE_PORT = 10002

class AzuriteContainer : GenericContainer<AzuriteContainer>(DEFAULT_IMAGE) {

    init {
        withExposedPorts(BLOB_PORT, QUEUE_PORT, TABLE_PORT)
        waitingFor(Wait.forLogMessage(".*service is successfully listening at.*", 3))
        withCommand("azurite", "--skipApiVersionCheck",
            "--blobHost", "0.0.0.0", "--blobPort", BLOB_PORT.toString(),
            "--queueHost", "0.0.0.0", "--queuePort", QUEUE_PORT.toString(),
            "--tableHost", "0.0.0.0", "--tablePort", TABLE_PORT.toString()
        )
    }

    val blobPort: Int
        get() = getMappedPort(BLOB_PORT)
    val blobServer: String
        get() = "${host}:${blobPort}"

    val queuePort: Int
        get() = getMappedPort(QUEUE_PORT)
    val queueServer: String
        get() = "${host}:${queuePort}"

    val tablePort: Int
        get() = getMappedPort(TABLE_PORT)
    val tableServer: String
        get() = "${host}:${tablePort}"

    val connectionString: String
        get() = format(
            "DefaultEndpointsProtocol=http;AccountName=devstoreaccount1;AccountKey=Eby8vdM02xNOcqFlqUwJPLlmEtlCDXJ1OUzFT50uSRZ6IFsuFq2UVErCz4I6tq/K1SZFPTOtr/KBHBeksoGMGw==;BlobEndpoint=http://%s:%d/devstoreaccount1;QueueEndpoint=http://%s:%d/devstoreaccount1;TableEndpoint=http://%s:%d/devstoreaccount1;",
            host, blobPort,
            host, queuePort,
            host, tablePort,
        )

}