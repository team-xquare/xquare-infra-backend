package xquare.app.xquareinfra.infrastructure.env.cloudflare

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(value = "cloudflare")
data class CloudflareProperties(
    val zoneId: String,
    val xAuthKey: String,
    val xAuthEmail: String
)