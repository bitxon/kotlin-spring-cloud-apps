package bitxon.cloud.springaws

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.context.DynamicPropertyRegistrar
import org.testcontainers.localstack.LocalStackContainer
import org.testcontainers.utility.DockerImageName
import org.testcontainers.utility.MountableFile

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfig {

    @Bean
    fun localStack(): LocalStackContainer {
        return LocalStackContainer(DockerImageName.parse("localstack/localstack:4.14.0"))
            .withServices("dynamodb", "s3", "sqs")
            .withCopyToContainer(MountableFile.forClasspathResource("scripts"), "/etc/localstack/init/ready.d")
    }

    @Bean
    fun propertiesOverride(localstack: LocalStackContainer): DynamicPropertyRegistrar {
        return DynamicPropertyRegistrar { registry ->
            registry.add("spring.cloud.aws.endpoint") { localstack.endpoint }
            registry.add("spring.cloud.aws.region.static") { localstack.region }
            registry.add("spring.cloud.aws.credentials.access-key") { localstack.accessKey }
            registry.add("spring.cloud.aws.credentials.secret-key") { localstack.secretKey }
            registry.add("spring.cloud.aws.s3.chunked-encoding-enabled") { false }
            registry.add("spring.cloud.aws.s3.path-style-access-enabled") { true }
        }
    }
}
