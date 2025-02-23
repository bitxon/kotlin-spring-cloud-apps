package bitxon.cloud.springgcp.storage

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.cloud.storage.Storage
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets.UTF_8


private const val BUCKET_NAME = "student-bucket"
private const val BLOB_NAME = "diplomas-archive.json"

@Service
class DiplomaStorage(
    private val storage: Storage,
    private val objectMapper: ObjectMapper
) {

    fun readDiplomasArchive(): List<Diploma> {
        val content = storage.get(BUCKET_NAME, BLOB_NAME).getContent().toString(UTF_8)
        return objectMapper.readValue(content)
    }

    fun writeDiplomasArchive(diplomas: List<Diploma>): List<Diploma> {
        val content = objectMapper.writeValueAsString(diplomas)
        storage.get(BUCKET_NAME).create(BLOB_NAME, content.toByteArray(UTF_8))
        return diplomas
    }
}