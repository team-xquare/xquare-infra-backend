package xquare.app.xquareinfra.infrastructure.telemtry.receiver

import io.grpc.stub.StreamObserver
import io.opentelemetry.proto.collector.trace.v1.ExportTraceServiceRequest
import io.opentelemetry.proto.collector.trace.v1.ExportTraceServiceResponse
import io.opentelemetry.proto.collector.trace.v1.TraceServiceGrpc
import io.opentelemetry.proto.trace.v1.ResourceSpans
import io.opentelemetry.proto.trace.v1.Span
import net.devh.boot.grpc.server.service.GrpcService
import org.springframework.context.ApplicationEventPublisher
import xquare.app.xquareinfra.infrastructure.telemtry.event.SpanReceivedEvent

@GrpcService
class TraceReceiver(private val eventPublisher: ApplicationEventPublisher) : TraceServiceGrpc.TraceServiceImplBase() {
    override fun export(request: ExportTraceServiceRequest, responseObserver: StreamObserver<ExportTraceServiceResponse>) {
        request.resourceSpansList.forEach { resourceSpans ->
            val resource = resourceSpans.resource
            val defaultServiceName = resource.attributesList
                .find { it.key == "service.name" }
                ?.value
                ?.stringValue

            resourceSpans.scopeSpansList.flatMap { it.spansList }
                .forEach { span ->
                    val rootServiceName = findRootServiceName(span, resourceSpans) ?: defaultServiceName
                    eventPublisher.publishEvent(SpanReceivedEvent(this, span, rootServiceName))
                }
        }

        val response = ExportTraceServiceResponse.getDefaultInstance()
        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }

    private fun findRootServiceName(span: Span, resourceSpans: ResourceSpans): String? {
        if (span.parentSpanId.isEmpty()) {
            return span.attributesList
                .find { it.key == "service.name" }
                ?.value
                ?.stringValue
        }

        val rootSpan = resourceSpans.scopeSpansList
            .flatMap { it.spansList }
            .find { it.parentSpanId.isEmpty() }

        return rootSpan?.attributesList
            ?.find { it.key == "service.name" }
            ?.value
            ?.stringValue
    }
}