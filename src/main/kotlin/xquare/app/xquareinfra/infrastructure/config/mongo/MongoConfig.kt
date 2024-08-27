package xquare.app.xquareinfra.infrastructure.config.mongo

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.mongodb.core.convert.MongoCustomConversions

@Configuration
class MongoConfig {
    @Bean
    fun customConversions(): MongoCustomConversions {
        val converters = listOf(
            StringToStringConverter()
        )
        return MongoCustomConversions(converters)
    }

    inner class StringToStringConverter : Converter<String, String> {
        override fun convert(source: String): String {
            return source.replace(".", "_")
        }
    }
}