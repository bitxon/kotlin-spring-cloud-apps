package bitxon.cloud.springazure.testutil

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer

class KafkaWriter(bootstrapServers: String, val topic: String, jaasConfig: String) {
    private val producer: KafkaProducer<String, String>

    init {
        val config = mapOf<String, Any>(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
            "security.protocol" to "SASL_PLAINTEXT",
            "sasl.mechanism" to "PLAIN",
            "sasl.jaas.config" to jaasConfig
        )
        this.producer = KafkaProducer(config, StringSerializer(), StringSerializer())
    }

    fun send(value: String) {
        val record = ProducerRecord<String, String>(topic, null, null, null, value, null)
        producer.send(record)
    }
}