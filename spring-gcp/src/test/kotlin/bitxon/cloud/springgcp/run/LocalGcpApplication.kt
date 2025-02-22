package bitxon.cloud.springgcp.run

import bitxon.cloud.springgcp.GcpApplication
import bitxon.cloud.springgcp.TestcontainersConfig
import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
	fromApplication<GcpApplication>().with(TestcontainersConfig::class).run(*args)
}
