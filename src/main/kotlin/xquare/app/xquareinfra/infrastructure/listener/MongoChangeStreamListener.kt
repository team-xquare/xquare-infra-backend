package xquare.app.xquareinfra.infrastructure.listener

import com.mongodb.client.model.changestream.ChangeStreamDocument
import com.mongodb.client.model.changestream.FullDocument
import org.bson.Document
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.mongodb.core.ChangeStreamOptions
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.messaging.ChangeStreamRequest
import org.springframework.data.mongodb.core.messaging.DefaultMessageListenerContainer
import org.springframework.data.mongodb.core.messaging.MessageListener
import org.springframework.stereotype.Component
import xquare.app.xquareinfra.adapter.out.persistence.trace.SpanMapper
import xquare.app.xquareinfra.adapter.out.persistence.trace.TraceMapper
import xquare.app.xquareinfra.application.trace.event.SpanEvent
import xquare.app.xquareinfra.infrastructure.persistence.trace.SpanMongoEntity
import xquare.app.xquareinfra.infrastructure.persistence.trace.TraceMongoEntity

@Component
class MongoChangeStreamListener(
    private val eventPublisher: ApplicationEventPublisher,
    private val mongoTemplate: MongoTemplate,
    private val spanMapper: SpanMapper
) {
    private val logger = LoggerFactory.getLogger(MongoChangeStreamListener::class.java)
    private val listenerContainer: DefaultMessageListenerContainer = DefaultMessageListenerContainer(mongoTemplate)

    init {
        startListening()
    }

    private fun startListening() {
        listenerContainer.start()

        val listener = MessageListener<ChangeStreamDocument<Document>, SpanMongoEntity> { message ->
            try {
                val span = message.body?.let { spanMapper.toModel(it) } ?: throw IllegalArgumentException("올바르지 않은 Span 입니다")
                eventPublisher.publishEvent(SpanEvent(this, span))
            } catch (ex: Exception) {
                logger.error("Change Stream 이벤트 처리 중 오류 발생", ex)
            }
        }

        val changeStreamOptions = ChangeStreamOptions.builder()
            .fullDocumentLookup(FullDocument.UPDATE_LOOKUP)
            .build()

        val options = ChangeStreamRequest.ChangeStreamRequestOptions(
            "tracing",
            "spans",
            changeStreamOptions
        )

        val request = ChangeStreamRequest(listener, options)

        listenerContainer.register(request, SpanMongoEntity::class.java)
    }
}