package bitxon.cloud.springgcp

import bitxon.cloud.springgcp.database.Student
import bitxon.cloud.springgcp.storage.Diploma
import bitxon.cloud.springgcp.testcontainers.PubSubEmulatorContainerExt
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
@SpringBootTest(classes = [GcpApplication::class], webEnvironment = RANDOM_PORT)
class GcpApplicationTests {

	@LocalServerPort
	private val port: Int = 0
	@Autowired
	lateinit var pubSubContainer: PubSubEmulatorContainerExt

	@BeforeEach
	fun setUp() {
		RestAssured.port = port
	}

	@Disabled("init not supported")
	@Test
	fun studentsFirestoreInit() {
	}

	@Test
	fun studentsFirestoreModify() {
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
	fun diplomasGcsInit() {
	}

	@Test
	fun diplomasGcsModify() {
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
	fun studentsPubSubInit() {
		val expectedStudent = Student("6", "Frank", "A") // Student pre-seeded via Pub/Sub init

		await().untilAsserted {
			val result = RestAssured
				.`when`().get("/students/${expectedStudent.id}")
				.then().statusCode(200).extract().`as`(object : TypeRef<Student>() {})
			assertThat(result).isEqualTo(expectedStudent)
		}
	}

	@Test
	fun studentsPubSubModify() {
		pubSubContainer.publish("""{"id":"101","name":"Tom","status":"A"}""")

		val expectedStudent = Student("101", "Tom", "A")

		await().untilAsserted {
			val result = RestAssured
				.`when`().get("/students/${expectedStudent.id}")
				.then().statusCode(200).extract().`as`(object : TypeRef<Student>() {})
			assertThat(result).isEqualTo(expectedStudent)
		}
	}

}
