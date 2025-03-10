package xquare.app.xquareinfra.adapter.out.external.github.env

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(value = "github")
data class GithubProperties(
    val token: String,
    val clientId: String,
    val clientSecret: String,
    val redirectUri: String,
)