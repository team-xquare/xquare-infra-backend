package xquare.app.xquareinfra.infrastructure.external.gocd.config

import feign.auth.BasicAuthRequestInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import xquare.app.xquareinfra.infrastructure.env.gocd.GocdProperties

@Configuration
class GocdAuthConfig(
    private val gocdProperties: GocdProperties
) {
    @Bean
    fun basicAuthRequestInterceptor(): BasicAuthRequestInterceptor {
        return BasicAuthRequestInterceptor(gocdProperties.username, gocdProperties.password)
    }
}