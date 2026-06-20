package bitxon.cloud.springaws


import bitxon.cloud.springaws.database.Student
import bitxon.cloud.springaws.storage.Diploma
import bitxon.cloud.springaws.testutil.SqsWriter
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
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Import

@Import(TestcontainersConfig::class)
@SpringBootTest(classes = [AwsApplication::class], webEnvironment = RANDOM_PORT)
class AwsApplicationTests {

    @LocalServerPort
    private val port: Int = 0
    @Autowired
    lateinit var sqsWriter: SqsWriter

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
    }

    @Test
    fun studentsDynamoDbInit() {
        val expectedStudent = Student("1", "Alice", "A")

        val result = RestAssured
            .`when`().get("/students/${expectedStudent.id}")
            .then().statusCode(200).extract().`as`(object : TypeRef<Student>() {})

        assertThat(result).isEqualTo(expectedStudent)
    }

    @Test
    fun studentsDynamoDbModify() {
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
    fun diplomasS3Init() {
    }

    @Test
    fun diplomasS3Modify() {
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

    @Test
    fun studentsSqsInit() {
        val expectedStudent = Student("6", "Frank", "A") // Student pre-seeded via SQS init script

        await().untilAsserted {
            val result = RestAssured
                .`when`().get("/students/${expectedStudent.id}")
                .then().statusCode(200).extract().`as`(object : TypeRef<Student>() {})
            assertThat(result).isEqualTo(expectedStudent)
        }
    }

    @Test
    fun studentsSqsModify() {
        sqsWriter.send("""{"id":"101","name":"Tom","status":"A"}""")

        val expectedStudent = Student("101", "Tom", "A")

        await().untilAsserted {
            val result = RestAssured
                .`when`().get("/students/${expectedStudent.id}")
                .then().statusCode(200).extract().`as`(object : TypeRef<Student>() {})
            assertThat(result).isEqualTo(expectedStudent)
        }
    }

}
