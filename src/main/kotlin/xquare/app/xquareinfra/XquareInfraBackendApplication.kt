package xquare.app.xquareinfra

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class XquareInfraBackendApplication

fun main(args: Array<String>) {
    runApplication<XquareInfraBackendApplication>(*args)
}
