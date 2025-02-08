package bitxon.cloud.springazure.storage

import com.azure.storage.blob.BlobClient
import com.azure.storage.blob.BlobContainerClient
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets.UTF_8

@Service
class DiplomaStorage(
    private val blobClient: BlobClient,
    private val blobContainerClient: BlobContainerClient,
    private val objectMapper: ObjectMapper
) {

    fun readDiplomasArchive(): List<Diploma> {
        val content = blobClient.downloadContent().toBytes().toString(UTF_8)
        return objectMapper.readValue(content)
    }

    fun writeDiplomasArchive(diplomas: List<Diploma>): List<Diploma> {
        createContainerIfNotExists() // TODO find a better way to create container before application starts
        val content = objectMapper.writeValueAsString(diplomas)
        blobClient.upload(content.byteInputStream(), true)
        return diplomas
    }

    private fun createContainerIfNotExists() {
        blobContainerClient.exists().let {
            if (!it) {
                blobContainerClient.create()
            }
        }
    }
}