package bitxon.cloud.springazure.run

import bitxon.cloud.springazure.AzureApplication
import bitxon.cloud.springazure.TestcontainersConfig
import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
    fromApplication<AzureApplication>().with(TestcontainersConfig::class).run(*args)
}
