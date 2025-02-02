package bitxon.cloud.springaws.run

import bitxon.cloud.springaws.AwsApplication
import bitxon.cloud.springaws.TestcontainersConfig
import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
    fromApplication<AwsApplication>()
        .with(TestcontainersConfig::class)
        .run(*args)
}
