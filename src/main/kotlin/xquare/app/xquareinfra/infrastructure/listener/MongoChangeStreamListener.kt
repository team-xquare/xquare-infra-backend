package xquare.app.xquareinfra.infrastructure.listener

import com.mongodb.client.model.changestream.ChangeStreamDocument
import org.bson.Document
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.mongodb.core.ChangeStreamOptions
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.messaging.ChangeStreamRequest
import org.springframework.data.mongodb.core.messaging.DefaultMessageListenerContainer
import org.springframework.data.mongodb.core.messaging.MessageListener
import org.springframework.stereotype.Component
import xquare.app.xquareinfra.adapter.out.persistence.trace.TraceMapper
import xquare.app.xquareinfra.application.trace.event.TraceEvent
import xquare.app.xquareinfra.domain.trace.model.Trace
import xquare.app.xquareinfra.infrastructure.persistence.trace.TraceMongoEntity

@Component
class MongoChangeStreamListener(
    private val eventPublisher: ApplicationEventPublisher,
    private val mongoTemplate: MongoTemplate,
    private val traceMapper: TraceMapper
) {

    private val listenerContainer: DefaultMessageListenerContainer = DefaultMessageListenerContainer(mongoTemplate)

    init {
        startListening()
    }

    private fun startListening() {
        listenerContainer.start()

        val listener = MessageListener<ChangeStreamDocument<Document>, TraceMongoEntity> { message ->
            val changEvent = message.body
            val trace = changEvent?.let { traceMapper.toModel(it) }
            trace?.let {
                if(trace.isError()) {
                    eventPublisher.publishEvent(TraceEvent(this, trace))
                }
            }
        }

        val changeStreamOptions = ChangeStreamOptions.builder()
            .build()

        val options = ChangeStreamRequest.ChangeStreamRequestOptions("tracing", "traces", changeStreamOptions)

        val request = ChangeStreamRequest(listener, options)

        listenerContainer.register(request, TraceMongoEntity::class.java)
    }
}