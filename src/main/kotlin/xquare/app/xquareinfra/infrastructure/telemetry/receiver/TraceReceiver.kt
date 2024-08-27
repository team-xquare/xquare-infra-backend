package xquare.app.xquareinfra.infrastructure.telemetry.receiver

import io.grpc.stub.StreamObserver
import io.opentelemetry.proto.collector.trace.v1.ExportTraceServiceRequest
import io.opentelemetry.proto.collector.trace.v1.ExportTraceServiceResponse
import io.opentelemetry.proto.collector.trace.v1.TraceServiceGrpc
import io.opentelemetry.proto.trace.v1.ResourceSpans
import io.opentelemetry.proto.trace.v1.Span
import net.devh.boot.grpc.server.service.GrpcService
import org.springframework.context.ApplicationEventPublisher
import xquare.app.xquareinfra.application.trace.port.out.SaveSpanPort
import xquare.app.xquareinfra.application.trace.port.out.SaveTracePort
import xquare.app.xquareinfra.domain.trace.model.Trace
import xquare.app.xquareinfra.infrastructure.telemetry.event.SpanReceivedEvent

@GrpcService
class TraceReceiver(
    private val eventPublisher: ApplicationEventPublisher,
    private val saveSpanPort: SaveSpanPort,
    private val saveTracePort: SaveTracePort
) : TraceServiceGrpc.TraceServiceImplBase() {
    override fun export(request: ExportTraceServiceRequest, responseObserver: StreamObserver<ExportTraceServiceResponse>) {
        request.resourceSpansList.forEach { resourceSpans ->
            val resource = resourceSpans.resource
            val defaultServiceName = resource.attributesList
                .find { it.key == "service.name" }
                ?.value
                ?.stringValue

            val rootSpan = findRootSpan(resourceSpans)
            val rootServiceName = rootSpan?.let { findServiceName(it) } ?: defaultServiceName

            val spans = resourceSpans.scopeSpansList.flatMap { it.spansList }
                .map { span ->
                    val domainSpan = xquare.app.xquareinfra.domain.trace.model.Span.createSpanFromOTel(span)
                    saveSpanPort.save(domainSpan)
                    eventPublisher.publishEvent(SpanReceivedEvent(this, span, rootServiceName))
                    domainSpan
                }

            val trace = Trace.createTraceFromSpans(spans, rootServiceName)
            saveTracePort.save(trace)
        }

        val response = ExportTraceServiceResponse.getDefaultInstance()
        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }

    private fun findRootSpan(resourceSpans: ResourceSpans): Span? {
        return resourceSpans.scopeSpansList
            .flatMap { it.spansList }
            .find { it.parentSpanId.isEmpty }
    }

    private fun findServiceName(span: Span): String? {
        return span.attributesList
            .find { it.key == "service.name" }
            ?.value
            ?.stringValue
    }
}