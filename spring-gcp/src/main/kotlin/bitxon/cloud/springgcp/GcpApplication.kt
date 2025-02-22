package bitxon.cloud.springgcp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GcpApplication

fun main(args: Array<String>) {
	runApplication<GcpApplication>(*args)
}
