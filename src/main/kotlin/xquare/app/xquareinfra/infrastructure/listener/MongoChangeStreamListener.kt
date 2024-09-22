package xquare.app.xquareinfra.infrastructure.listener

import com.mongodb.client.model.changestream.ChangeStreamDocument
import com.mongodb.client.model.changestream.FullDocument
import org.bson.Document
import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.mongodb.core.ChangeStreamOptions
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.messaging.ChangeStreamRequest
import org.springframework.data.mongodb.core.messaging.DefaultMessageListenerContainer
import org.springframework.data.mongodb.core.messaging.MessageListener
import org.springframework.stereotype.Component
import xquare.app.xquareinfra.adapter.out.persistence.trace.TraceMapper
import xquare.app.xquareinfra.application.trace.event.TraceEvent
import xquare.app.xquareinfra.application.trace.port.out.FindTraceEventCachePort
import xquare.app.xquareinfra.application.trace.port.out.FindTracePort
import xquare.app.xquareinfra.application.trace.port.out.SaveTraceEventCachePort
import xquare.app.xquareinfra.application.trace.port.out.SaveTracePort
import xquare.app.xquareinfra.infrastructure.persistence.trace.TraceMongoEntity
import java.util.concurrent.TimeUnit

@Component
class MongoChangeStreamListener(
    private val eventPublisher: ApplicationEventPublisher,
    private val mongoTemplate: MongoTemplate,
    private val traceMapper: TraceMapper,
    private val findTraceEventCachePort: FindTraceEventCachePort,
    private val saveTraceEventCachePort: SaveTraceEventCachePort,
    private val redissonClient: RedissonClient
) {
    private val logger = LoggerFactory.getLogger(MongoChangeStreamListener::class.java)
    private val listenerContainer: DefaultMessageListenerContainer = DefaultMessageListenerContainer(mongoTemplate)

    init {
        startListening()
    }

    private fun startListening() {
        listenerContainer.start()

        val listener = MessageListener<ChangeStreamDocument<Document>, TraceMongoEntity> { message ->
            try {
                val changeEvent = message.body
                val trace = changeEvent?.let { traceMapper.toModel(it) } ?: throw IllegalArgumentException("Trace Not Found")
                val traceId = trace.traceId

                if (!findTraceEventCachePort.existsById(traceId)) {
                    eventPublisher.publishEvent(TraceEvent(this, trace))
                    saveTraceEventCachePort.save(TraceEventCache(traceId = traceId, ttl = 5))
                } else {
                    logger.info("이미 처리된 이벤트: {}", traceId)
                }
            } catch (ex: Exception) {
                logger.error("Change Stream 이벤트 처리 중 오류 발생", ex)
            }
        }

        val changeStreamOptions = ChangeStreamOptions.builder()
            .fullDocumentLookup(FullDocument.UPDATE_LOOKUP)
            .build()

        val options = ChangeStreamRequest.ChangeStreamRequestOptions(
            "tracing",
            "traces",
            changeStreamOptions
        )

        val request = ChangeStreamRequest(listener, options)

        listenerContainer.register(request, TraceMongoEntity::class.java)
    }
}