package bitxon.cloud.springazure

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AzureApplication

fun main(args: Array<String>) {
    System.setProperty("COSMOS.HTTP_CONNECTION_WITHOUT_TLS_ALLOWED", "true") // DO NOT USE IN PRODUCTION
    runApplication<AzureApplication>(*args)
}
