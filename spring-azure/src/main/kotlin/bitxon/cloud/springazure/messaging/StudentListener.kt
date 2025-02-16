package bitxon.cloud.springazure.messaging

import bitxon.cloud.springazure.database.Student
import bitxon.cloud.springazure.database.StudentRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service


@Service
class StudentListener(
    private val studentRepository: StudentRepository
) {
    private val logger: Logger = LoggerFactory.getLogger(StudentListener::class.java)


    @KafkaListener(
        topics = ["students-eventhub"],
        properties = ["spring.json.value.default.type=bitxon.cloud.springazure.database.Student"],
        concurrency = "2")
    fun listen(@Payload student: Student) {
        logger.info("Received[EventHub] student: $student")
        studentRepository.save(student)
    }
}