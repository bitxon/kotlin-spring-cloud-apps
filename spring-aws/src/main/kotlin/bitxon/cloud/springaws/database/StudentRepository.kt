package bitxon.cloud.springaws.database

import io.awspring.cloud.dynamodb.DynamoDbTemplate
import org.springframework.stereotype.Repository
import software.amazon.awssdk.enhanced.dynamodb.Key

@Repository
class StudentRepository(
    private val database: DynamoDbTemplate
) {
    fun save(student: Student): Student {
        return database.save<Student>(student)
    }

    fun findById(id: String): Student? {
        val key: Key = Key.builder().partitionValue(id).build()
        return database.load(key, Student::class.java)
    }
}