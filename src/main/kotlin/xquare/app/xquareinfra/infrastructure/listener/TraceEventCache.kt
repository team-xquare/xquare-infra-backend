package xquare.app.xquareinfra.infrastructure.listener

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive

@RedisHash(value = "trace_event_cache")
class TraceEventCache(
    traceId: String,
    ttl: Long
) {
    @Id
    var traceId: String = traceId
        protected set

    @TimeToLive
    var ttl: Long = ttl
        protected set
}