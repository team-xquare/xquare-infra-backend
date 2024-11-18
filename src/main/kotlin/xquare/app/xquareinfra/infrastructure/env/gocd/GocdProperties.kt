package xquare.app.xquareinfra.infrastructure.env.gocd

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("gocd")
data class GocdProperties(
    val username: String,
    val password: String
)