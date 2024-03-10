package xquare.app.xquareinfra

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class XquareInfraBackendApplication

fun main(args: Array<String>) {
    runApplication<XquareInfraBackendApplication>(*args)
}
