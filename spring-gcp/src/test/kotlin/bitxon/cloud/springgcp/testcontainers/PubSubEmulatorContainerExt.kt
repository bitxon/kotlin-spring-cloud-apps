package bitxon.cloud.springgcp.testcontainers

import com.github.dockerjava.api.command.InspectContainerResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.client.RestClient
import org.testcontainers.containers.PubSubEmulatorContainer
import org.testcontainers.utility.DockerImageName
import java.util.Base64


class PubSubEmulatorContainerExt(image: DockerImageName) : PubSubEmulatorContainer(image) {

    override fun containerIsStarted(containerInfo: InspectContainerResponse?) {
        super.containerIsStarted(containerInfo)
        initData("your-project-id", "student-updates", "student-updates-subscription")
    }

    private fun initData(projectId: String, topic: String, subscription: String) {
        var restClient = RestClient.builder().baseUrl("http://$emulatorEndpoint/v1/projects/$projectId").build()

        restClient.put()
            .uri("/topics/$topic")
            .retrieve()
            .toBodilessEntity()
        println("Topic created successfully")

        restClient.put()
            .uri("/subscriptions/$subscription")
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .body(mapOf("topic" to "projects/$projectId/topics/$topic"))
            .retrieve()
            .toBodilessEntity()
        println("Subscription created successfully")

        restClient.post()
            .uri("/topics/$topic:publish")
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .body(mapOf("messages" to listOf(
                mapOf("data" to encodeBase64("""{"id":"6", "name":"Frank", "status":"A"}""")),
                mapOf("data" to encodeBase64("""{"id":"7", "name":"Grace", "status":"G"}""")),
                mapOf("data" to encodeBase64("""{"id":"8", "name":"Helen", "status":"I"}""")),
                mapOf("data" to encodeBase64("""{"id":"9", "name":"Ivan", "status":"E"}""")),
                mapOf("data" to encodeBase64("""{"id":"10", "name":"Jack", "status":"A"}"""))
            )))
            .retrieve()
            .toBodilessEntity()
        println("Messages are published successfully")

    }

    private fun encodeBase64(value: String): String {
        return Base64.getEncoder().encodeToString(value.toByteArray())
    }
}

