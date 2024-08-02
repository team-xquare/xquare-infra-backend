package xquare.app.xquareinfra.infrastructure.opentelemtry.event

import io.opentelemetry.proto.trace.v1.Span
import org.springframework.context.ApplicationEvent

class SpanReceivedEvent(
    source: Any,
    val span: Span,
    val rootServiceName: String?) : ApplicationEvent(source)