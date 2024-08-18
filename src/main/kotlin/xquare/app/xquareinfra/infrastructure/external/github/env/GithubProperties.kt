package xquare.app.xquareinfra.infrastructure.external.github.env

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(value = "github")
data class GithubProperties(
    val token: String
)