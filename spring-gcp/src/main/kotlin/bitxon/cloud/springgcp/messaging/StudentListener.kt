package bitxon.cloud.springgcp.messaging

import bitxon.cloud.springgcp.database.Student
import bitxon.cloud.springgcp.database.StudentRepository
import com.google.cloud.spring.pubsub.core.PubSubTemplate
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage
import org.springframework.stereotype.Service
import tools.jackson.databind.ObjectMapper

@Service
class StudentListener(
    private val pubSubTemplate: PubSubTemplate,
    private val repository: StudentRepository,
    private val objectMapper: ObjectMapper
) {

    init {
        pubSubTemplate.subscribe("student-updates-subscription", this::handleMessage)
    }

    fun handleMessage(message: BasicAcknowledgeablePubsubMessage) {
        handleStudent(message.pubsubMessage.data.toStringUtf8())
        message.ack()
    }

    fun handleStudent(message: String) {
        val student = objectMapper.readValue(message, Student::class.java)
        println("Received student: $student")
        repository.save(student).block()
    }

}