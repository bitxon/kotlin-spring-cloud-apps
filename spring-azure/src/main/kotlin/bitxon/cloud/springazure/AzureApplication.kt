package bitxon.cloud.springazure

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AzureApplication

fun main(args: Array<String>) {
    runApplication<AzureApplication>(*args)
}
