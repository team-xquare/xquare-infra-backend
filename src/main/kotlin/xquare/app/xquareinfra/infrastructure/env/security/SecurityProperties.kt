package xquare.app.xquareinfra.infrastructure.env.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("server.domain")
data class SecurityProperties(

    val frontDomain: String,

    val backDomain: String
)
