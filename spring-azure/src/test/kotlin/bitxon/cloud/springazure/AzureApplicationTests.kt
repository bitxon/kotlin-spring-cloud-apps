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
    fun studentsCosmosDbInit() {
        val expectedStudent = Student("1", "Alice", "A")

        val result = RestAssured
            .`when`().get("/students/${expectedStudent.id}")
            .then().statusCode(200).extract().`as`(object : TypeRef<Student>() {})

        assertThat(result).isEqualTo(expectedStudent)
    }

    @Test
    fun studentsCosmosDbModify() {
        val student = Student("100", "John Doe", "A")

        RestAssured
            .given().body(student).contentType(ContentType.JSON)
            .`when`().post("/students")
            .then().statusCode(200)

        val result = RestAssured
            .`when`().get("/students/${student.id}")
            .then().statusCode(200).extract().`as`(object : TypeRef<Student>() {})

        assertThat(result).isEqualTo(student)
    }

    @Disabled("init not supported")
    @Test
    fun diplomasBlobStorageInit() {
    }

    @Test
    fun diplomasBlobStorageModify() {
        val diplomas = listOf(
            Diploma("John Doe1", 1992),
            Diploma("Jane Doe2", 2005)
        )

        RestAssured
            .given().body(diplomas).contentType(ContentType.JSON)
            .`when`().post("/diplomas")
            .then().statusCode(200)

        val results = RestAssured
            .`when`().get("/diplomas")
            .then().statusCode(200).extract().`as`(object : TypeRef<List<Diploma>>() {})

        assertThat(results).containsExactlyInAnyOrderElementsOf(diplomas)
    }

    // EventHub emulator v2.2.0 does not support Kafka 4.x protocol (kafka-clients 4.1.1
    // pulled in by spring-boot-starter-kafka via Spring Boot 4.0.x). The ApiVersionsRequest handshake
    // fails immediately, making it impossible to produce or consume messages via Kafka against the emulator.
    // Track: https://github.com/Azure/azure-event-hubs-emulator-installer/issues/72
    @Disabled("EventHub emulator v2.2.0 does not support Kafka 4.x protocol")
    @Test
    fun studentsEventHubInit() {
    }

    @Disabled("EventHub emulator v2.2.0 does not support Kafka 4.x protocol")
    @Test
    fun studentsEventHubModify() {
        kafkaWriter.send("""{"id":"101","name":"Tom","status":"A"}""")

        val expectedStudent = Student("101", "Tom", "A")

        await().untilAsserted {
            val result = RestAssured
                .`when`().get("/students/${expectedStudent.id}")
                .then().statusCode(200).extract().`as`(object : TypeRef<Student>() {})
            assertThat(result).isEqualTo(expectedStudent)
        }
    }

}
