package bitxon.cloud.springgcp

import bitxon.cloud.springgcp.database.Student
import bitxon.cloud.springgcp.storage.Diploma
import io.restassured.RestAssured
import io.restassured.common.mapper.TypeRef
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Import

@Import(TestcontainersConfig::class)
@SpringBootTest(classes = [GcpApplication::class], webEnvironment = RANDOM_PORT)
class GcpApplicationTests {

	@LocalServerPort
	private val port: Int = 0

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

}
