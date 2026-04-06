package bitxon.cloud.springaws

import io.floci.testcontainers.FlociContainer
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName
import org.testcontainers.utility.MountableFile

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfig {

    @Bean
    @ServiceConnection
    fun floci(): FlociContainer {
        return FlociContainer(DockerImageName.parse("floci/floci:1.5.10-aws"))
            .withCopyToContainer(MountableFile.forClasspathResource("scripts"), "/etc/floci/init/start.d")
            // Floci runs init scripts async after HTTP is up; wait for the last script's output
            // to ensure all resources (DynamoDB table, S3 bucket, SQS queue) are created before tests start
            .waitingFor(Wait.forLogMessage(".*SQS queue created.*", 1))
    }
}