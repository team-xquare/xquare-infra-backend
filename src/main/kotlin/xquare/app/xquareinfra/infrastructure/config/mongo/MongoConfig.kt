package xquare.app.xquareinfra.infrastructure.config.mongo

import com.mongodb.MongoClientSettings
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import xquare.app.xquareinfra.infrastructure.env.mongo.MongoProperties

@Configuration
class DocumentDBConf(private val mongoProperties: MongoProperties) {
    companion object {
        const val KEY_STORE_FILE = "rds-truststore.jks"
    }

    @Bean
    fun mongoClientSettings(): MongoClientSettings {
        setSslProperties()
        return MongoClientSettings.builder()
            .applyToSslSettings { builder -> builder.enabled(true) }
            .build()
    }

    private fun setSslProperties() {
        val resource = ClassPathResource(KEY_STORE_FILE)
        val keyStorePath = resource.file.absolutePath
        System.setProperty("javax.net.ssl.trustStore", keyStorePath)
        System.setProperty("javax.net.ssl.trustStorePassword", mongoProperties.jksPassword)
    }
}