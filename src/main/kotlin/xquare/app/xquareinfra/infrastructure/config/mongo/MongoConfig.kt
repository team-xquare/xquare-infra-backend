package xquare.app.xquareinfra.infrastructure.config.mongo

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.convert.DbRefResolver
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver
import org.springframework.data.mongodb.core.convert.MappingMongoConverter
import org.springframework.data.mongodb.core.mapping.MongoMappingContext
import org.springframework.data.mongodb.MongoDatabaseFactory

@Configuration
class MongoConfig {

    @Bean
    fun mongoConverter(
        mongoDbFactory: MongoDatabaseFactory,
        mongoMappingContext: MongoMappingContext
    ): MappingMongoConverter {
        val dbRefResolver: DbRefResolver = DefaultDbRefResolver(mongoDbFactory)
        val mongoConverter = MappingMongoConverter(dbRefResolver, mongoMappingContext)
        mongoConverter.setMapKeyDotReplacement("#DOT#")
        return mongoConverter
    }
}