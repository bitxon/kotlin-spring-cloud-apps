package bitxon.cloud.springaws.messaging

import bitxon.cloud.springaws.database.Student
import bitxon.cloud.springaws.database.StudentRepository
import io.awspring.cloud.sqs.annotation.SqsListener
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service


@Service
class StudentListener(
    private val studentRepository: StudentRepository
) {
    private val logger: Logger = LoggerFactory.getLogger(StudentListener::class.java)


    @SqsListener(id = "student-listener", queueNames = ["student-queue"])
    fun listen(student: Student) {
        logger.info("Received[SQS] student: $student")
        studentRepository.save(student)
    }
}