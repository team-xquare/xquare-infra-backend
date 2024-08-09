package xquare.app.xquareinfra.infrastructure.external.feign.config

import feign.Feign
import feign.Logger
import feign.Request
import feign.codec.ErrorDecoder
import feign.httpclient.ApacheHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import xquare.app.xquareinfra.infrastructure.external.feign.config.error.FeignClientErrorDecoder
import xquare.app.xquareinfra.infrastructure.http.HttpKeepAliveStrategy

@EnableFeignClients(basePackages = ["xquare.app.xquareinfra"])
@Configuration
@Import(FeignClientErrorDecoder::class)
class FeignConfig(
    private val keepAliveStrategy: HttpKeepAliveStrategy
) {

    @Bean
    fun feignLoggerLevel(): Logger.Level = Logger.Level.FULL

    @Bean
    @ConditionalOnMissingBean(value = [ErrorDecoder::class]) //Bean 오버라이딩시 충돌 해결
    fun commonFeignErrorDecoder(): FeignClientErrorDecoder = FeignClientErrorDecoder()

    @Bean
    fun feignBuilder(): Feign.Builder {
        val httpClient = HttpClientBuilder.create()
            .setKeepAliveStrategy(keepAliveStrategy)
            .build()

        return Feign.builder()
            .client(ApacheHttpClient(httpClient))
            .options(Request.Options(10000, 60000)) // connectTimeout = 10s, readTimeout = 60s
    }
}
