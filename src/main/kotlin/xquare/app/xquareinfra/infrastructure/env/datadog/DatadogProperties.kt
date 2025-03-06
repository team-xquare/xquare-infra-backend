package xquare.app.xquareinfra.infrastructure.env.datadog

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("datadog")
@ConstructorBinding
data class DatadogProperties(
    val apiKey: String,
    val appKey: String
)