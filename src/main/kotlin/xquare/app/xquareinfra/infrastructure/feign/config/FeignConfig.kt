package xquare.app.xquareinfra.infrastructure.feign.config

import feign.Logger
import feign.codec.ErrorDecoder
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import xquare.app.xquareinfra.infrastructure.feign.config.error.FeignClientErrorDecoder

@EnableFeignClients(basePackages = ["xquare.app.xquareinfra"])
@Configuration
@Import(FeignClientErrorDecoder::class)
class FeignConfig {

    @Bean
    fun feignLoggerLevel(): Logger.Level = Logger.Level.FULL

    @Bean
    @ConditionalOnMissingBean(value = [ErrorDecoder::class]) //Bean 오버라이딩시 충돌 해결
    fun commonFeignErrorDecoder(): FeignClientErrorDecoder = FeignClientErrorDecoder()
}
