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
import xquare.app.xquareinfra.adapter.out.persistence.trace.TraceMapper
import xquare.app.xquareinfra.application.trace.event.TraceEvent
import xquare.app.xquareinfra.infrastructure.persistence.trace.TraceMongoEntity

@Component
class MongoChangeStreamListener(
    private val eventPublisher: ApplicationEventPublisher,
    private val mongoTemplate: MongoTemplate,
    private val traceMapper: TraceMapper
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
                val trace = changeEvent?.let { traceMapper.toModel(it) }
                trace?.let {
                    eventPublisher.publishEvent(TraceEvent(this, it))
                }
                logger.info("Change Stream 이벤트 처리 완료: {}", trace)
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