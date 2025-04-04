package xquare.app.xquareinfra.infrastructure.env.http

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("url")
data class UrlProperties(
    val deploy: String,
    val log: String,
    val gocd: String,
    val cloudflare: String
)