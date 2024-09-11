package xquare.app.xquareinfra.infrastructure.env.mongo

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("mongodb")
data class MongoProperties(
    val jksPassword: String
)