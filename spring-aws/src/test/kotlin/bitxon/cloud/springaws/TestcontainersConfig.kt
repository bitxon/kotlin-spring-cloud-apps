package bitxon.cloud.springaws

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.test.context.DynamicPropertyRegistry
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.utility.DockerImageName
import org.testcontainers.utility.MountableFile

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfig {

    @Bean
    fun localStack(registry: DynamicPropertyRegistry): LocalStackContainer {
        val container = LocalStackContainer(DockerImageName.parse("localstack/localstack:latest"))
            .withServices(
                LocalStackContainer.Service.DYNAMODB,
                LocalStackContainer.Service.S3,
                LocalStackContainer.Service.SQS
            )
            .withCopyToContainer(MountableFile.forClasspathResource("scripts"), "/etc/localstack/init/ready.d")

        registry.add("spring.cloud.aws.endpoint") { container.endpoint }
        registry.add("spring.cloud.aws.region.static") { container.region }
        registry.add("spring.cloud.aws.credentials.access-key") { container.accessKey }
        registry.add("spring.cloud.aws.credentials.secret-key") { container.secretKey }
        registry.add("spring.cloud.aws.s3.chunked-encoding-enabled") { false }
        registry.add("spring.cloud.aws.s3.path-style-access-enabled") { true }

        return container
    }
}
