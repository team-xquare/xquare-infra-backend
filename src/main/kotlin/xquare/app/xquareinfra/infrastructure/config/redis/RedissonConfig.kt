package xquare.app.xquareinfra.infrastructure.config.redis

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.redisson.spring.data.connection.RedissonConnectionFactory
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.Scope
import org.springframework.data.redis.core.RedisKeyValueAdapter
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import xquare.app.xquareinfra.infrastructure.env.redis.RedisProperties


@EnableRedisRepositories(
    enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP,
    keyspaceNotificationsConfigParameter = "" // Elastic Cache는 CONFIG 명령어를 허용하지 않는 이슈
)
@Configuration
class RedissonConfig(
    private val properties: RedisProperties
) {
    @Bean(destroyMethod = "shutdown")
    fun redisson(): RedissonClient {
        val config = Config()
        config.useSingleServer()
            .setAddress("redis://${properties.host}:${properties.port}")
        return Redisson.create(config)
    }

    @Bean
    @DependsOn("redisson")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    fun redissonConnectionFactory(redissonClient: RedissonClient?): RedissonConnectionFactory? {
        return RedissonConnectionFactory(redissonClient)
    }
}
