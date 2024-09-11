package xquare.app.xquareinfra.infrastructure.config.mongo

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration
import org.springframework.data.mongodb.core.MongoTemplate

@Configuration
class MongoConfig : AbstractMongoClientConfiguration() {
    @Value("\${spring.data.mongodb.uri}")
    private lateinit var mongoUri: String

    override fun getDatabaseName(): String = "tracing"

    @Bean
    override fun mongoClient(): MongoClient {
        val connectionString = ConnectionString(mongoUri)
        val mongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .build()
        return MongoClients.create(mongoClientSettings)
    }

    @Bean
    fun mongoTemplate(): MongoTemplate = MongoTemplate(mongoClient(), databaseName)
}