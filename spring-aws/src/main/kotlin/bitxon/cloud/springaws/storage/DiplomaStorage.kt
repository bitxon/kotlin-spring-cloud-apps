package bitxon.cloud.springaws.storage

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.awspring.cloud.s3.S3Template
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets.UTF_8


private const val BUCKET_NAME = "student-bucket"
private const val FILE_NAME = "diplomas-archive.json"

@Service
class DiplomaStorage(
    private val s3Template: S3Template,
    private val objectMapper: ObjectMapper
) {

    fun readStudentsArchive(): List<Diploma> {
        val content = s3Template.download(BUCKET_NAME, FILE_NAME).getContentAsString(UTF_8)
        return objectMapper.readValue(content)
    }

    fun writeStudentsArchive(diplomas: List<Diploma>): List<Diploma> {
        val content = objectMapper.writeValueAsString(diplomas)
        s3Template.upload(BUCKET_NAME, FILE_NAME, content.byteInputStream())
        return diplomas
    }
}