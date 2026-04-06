package bitxon.cloud.springazure

import bitxon.cloud.springazure.database.Student
import bitxon.cloud.springazure.storage.Diploma
import bitxon.cloud.springazure.testutil.KafkaWriter
import io.restassured.RestAssured
import io.restassured.common.mapper.TypeRef
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Import

@Import(TestcontainersConfig::class)
@SpringBootTest(classes = [AzureApplication::class], webEnvironment = RANDOM_PORT)
class AzureApplicationTests {

    @LocalServerPort
    private val port: Int = 0
    @Autowired
    lateinit var kafkaWriter: KafkaWriter

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
    }

    @Test
    fun diplomas() {
        val diplomas = listOf(
            Diploma("John Doe1", 1992),
            Diploma("Jane Doe2", 2005)
        )

        // Create diplomas
        RestAssured
            .given().body(diplomas).contentType(ContentType.JSON)
            .`when`().post("/diplomas")
            .then().statusCode(200)

        // Get diplomas
        val results = RestAssured
            .`when`().get("/diplomas")
            .then().statusCode(200).extract().`as`(object : TypeRef<List<Diploma>>() {})

        assertThat(results).containsExactlyInAnyOrderElementsOf(diplomas)
    }

    @Test
    fun students() {
        val student = Student("1", "John Doe", "A")

        // Create student
        RestAssured
            .given().body(student).contentType(ContentType.JSON)
            .`when`().post("/students")
            .then().statusCode(200)

        // Get student
        val result = RestAssured
            .`when`().get("/students/${student.id}")
            .then().statusCode(200).extract().`as`(object : TypeRef<Student>() {})

        assertThat(result).isEqualTo(student)
    }

    // Disabled: EventHub emulator v2.1.0 does not support Kafka 4.x protocol (kafka-clients 4.1.1
    // pulled in by spring-boot-starter-kafka via Spring Boot 4.0.x). The ApiVersionsRequest handshake
    // fails immediately, making it impossible to produce or consume messages via Kafka against the emulator.
    @Disabled
    @Test
    fun studentsFromEventHub() {
        kafkaWriter.send("""{"id":"6","name":"Frank","status":"A"}""") // TODO find a way to do this via init script

        val expectedStudent = Student("6", "Frank", "A") // Student created in init script for EventHub

        await().untilAsserted {
            val result = RestAssured
                .`when`().get("/students/${expectedStudent.id}")
                .then().statusCode(200).extract().`as`(object : TypeRef<Student>() {})
            assertThat(result).isEqualTo(expectedStudent)
        }
    }

}
