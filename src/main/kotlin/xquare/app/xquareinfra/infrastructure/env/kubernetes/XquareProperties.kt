package xquare.app.xquareinfra.infrastructure.env.kubernetes

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("xquare")
@ConstructorBinding
data class XquareProperties(
    val accessKey: String,
    val secretKey: String,
    val gatewayDns: String
)